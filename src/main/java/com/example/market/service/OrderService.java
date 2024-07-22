package com.example.market.service;

import com.example.market.domain.*;
import com.example.market.dto.CartOrderDto;
import com.example.market.dto.OrderDto;
import com.example.market.dto.OrderHistDto;
import com.example.market.dto.OrderItemDto;
import com.example.market.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final UserAccountRepository userAccountRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

// 추가 1 --
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String userId){
        List<OrderDto> orderDtoList = new ArrayList<>();

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

//        Long orderId = orderService.orders(orderDtoList, userId);
        Long orderId = orders(orderDtoList, userId);
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
        return orderId;
    }
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String userId){
        UserAccount userAccount = userAccountRepository.findByUserId(userId).orElseThrow();
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        UserAccount user = cartItem.getCart().getUserAccount();

        if(!StringUtils.equals(userAccount.getEmail(), user.getEmail())){
            return false;
        }
        return true;
    }

    public Long order(OrderDto orderDto, String userId){
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        UserAccount userAccount = userAccountRepository.findByUserId(userId).orElseThrow();

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(userAccount, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    // 주문 내역 조회
    @Transactional(readOnly = true)
    public List<OrderHistDto> getOrderList(String userId) {


        // orders  : 사용자 ID와 페이지 정보를 기반으로 Order 엔티티 목록 조회
        // totalCount : 사용자 ID에 해당하는 전체 주문 수 조회. 페이징 처리를 위해 필요
        List<Order> orders = orderRepository.findByUserAccount_UserIdOrderByOrderDateDesc(userId);
//        Long totalCount = orderRepository.countByUserAccount_UserId(userId);

        // orderHistDtos : 최종적으로 반환할 주문 내역 DTO 목록을 저장할 리스트
        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        //  for (Order order : orders) : 조회된 각 Order 엔티티에 대해 반복
        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);  // 각 주문 엔티티를 기반으로 OrderHistDto 객체를 생성

            List<OrderItem> orderItems = order.getOrderItems();   // 주문에 포함된 주문 항목 목록 조회
            for (OrderItem orderItem : orderItems) {              // 각 주문 항목에 대해 반복

                OrderItemDto orderItemDto = OrderItemDto.from(orderItem);  // OrderItem을 OrderItemDto로 변환
                orderHistDto.addOrderItemDto(orderItemDto);       // OrderHistDto에 OrderItemDto를 추가
            }
            orderHistDtos.add(orderHistDto);                      // OrderHistDto 리스트에 추가
        }
        return orderHistDtos;
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String userId){
        UserAccount userAccount = userAccountRepository.findByUserId(userId).orElseThrow();
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        UserAccount user = order.getUserAccount();

        if(!StringUtils.equals(userAccount.getEmail(), user.getEmail())){
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }


    public Long orders(List<OrderDto> orderDtoList, String userId){

        UserAccount userAccount = userAccountRepository.findByUserId(userId).get();
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }
        Order order = Order.createOrder(userAccount, orderItemList);

        orderRepository.save(order);
        return order.getId();
    }

}