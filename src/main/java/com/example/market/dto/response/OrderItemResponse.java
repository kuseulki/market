package com.example.market.dto.response;

import com.example.market.dto.OrderItemDto;

public record OrderItemResponse(
        String itemName,
        int count,
        int orderPrice,
        String savePath //  이미지

) {
    public static OrderItemResponse of( String itemName, int count, int orderPrice, String savePath) {
        return new OrderItemResponse(itemName, count, orderPrice, savePath);
    }

    public static OrderItemResponse from(OrderItemDto dto) {

        String firstImagePath = null;
        if (dto.itemImageDtos() != null && !dto.itemImageDtos().isEmpty()) {
            firstImagePath = dto.itemImageDtos().get(0).storedFileName();
        }

        return new OrderItemResponse(
                dto.itemName(),
                dto.count(),
                dto.orderPrice(),
                firstImagePath
        );
    }

}
