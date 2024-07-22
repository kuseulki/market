package com.example.market.service;

import com.example.market.domain.Order;
import com.example.market.domain.type.PaymentStatus;
import com.example.market.dto.RequestPayDto;
import com.example.market.dto.request.PaymentCallbackRequest;
import com.example.market.repository.OrderRepository;
import com.example.market.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;

    // 결제 요청 데이터 조회
    public RequestPayDto findRequestDto(Long id) {

        Order order = orderRepository.findOrderAndPaymentAndUserAccount(id).orElseThrow(() -> new IllegalArgumentException("주문이 없습니다."));

        // 첫 번째 OrderItem의 ItemName을 가져오기
        String itemName = order.getOrderItems().isEmpty() ? null : order.getOrderItems().get(0).getItem().getItemName();

        return RequestPayDto.of(
                order.getId(),
                itemName,
                order.getTotalPrice(),
//                order.getPayment().getPrice(),
                order.getUserAccount().getEmail(),
                order.getUserAccount().getUserName(),
                order.getUserAccount().getPhone(),
                order.getUserAccount().getAddr() + order.getUserAccount().getAddrDetail(),
                order.getUserAccount().getZipCode()
        );
    }

    // 결제(콜백)
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request) {

        try {
            // 결제 단건 조회(아임포트)
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPaymentUid());
            // 주문내역 조회
            Order order = orderRepository.findOrderAndPayment(request.getOrderUid())
                    .orElseThrow(() -> new IllegalArgumentException("주문 내역이 없습니다."));

            // 결제 완료가 아니면
            if(!iamportResponse.getResponse().getStatus().equals("paid")) {
                // 주문, 결제 삭제
                orderRepository.delete(order);
                paymentRepository.delete(order.getPayment());

                throw new RuntimeException("결제 미완료");
            }

            // DB에 저장된 결제 금액
            Long price = order.getPayment().getPrice();
            // 실 결제 금액
            int iamportPrice = iamportResponse.getResponse().getAmount().intValue();

            // 결제 금액 검증
            if(iamportPrice != price) {
                // 주문, 결제 삭제
                orderRepository.delete(order);
                paymentRepository.delete(order.getPayment());

                // 결제금액 위변조로 의심되는 결제금액을 취소(아임포트)
                iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));

                throw new RuntimeException("결제금액 위변조 의심");
            }

            // 결제 상태 변경
            order.getPayment().changePaymentBySuccess(PaymentStatus.OK, iamportResponse.getResponse().getImpUid());

            return iamportResponse;

        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
