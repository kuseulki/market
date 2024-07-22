package com.example.market.dto;

import com.example.market.domain.Order;
import com.example.market.domain.OrderItem;
import com.example.market.domain.type.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Getter @Setter
public class OrderHistDto {

    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;

    private String savePath; // 이미지 경로
    private String firstImagePath; // 첫 번째 이미지 경로

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
        // 첫 번째 이미지 경로 설정
        if (firstImagePath == null && orderItemDto.itemImageDtos() != null && !orderItemDto.itemImageDtos().isEmpty()) {
            firstImagePath = orderItemDto.itemImageDtos().get(0).storedFileName();
        }
    }


    public OrderHistDto(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
        // 주문의 첫 번째 아이템의 이미지를 savePath에 설정 (예시로 설정)
        if (!order.getOrderItems().isEmpty()) {
            OrderItem firstOrderItem = order.getOrderItems().get(0);
            if (!firstOrderItem.getItem().getItemimages().isEmpty()) {
                this.savePath = firstOrderItem.getItem().getItemimages().get(0).getSavePath();
            }
        }
    }
}
