package com.example.market.dto;

import com.example.market.domain.OrderItem;

import java.util.List;
import java.util.stream.Collectors;


public record OrderItemDto(

     String itemName,
     int count,
     int orderPrice,
     String savePath, // 이미지 경로 추가
     List<ItemImageDto> itemImageDtos
) {

    public static OrderItemDto of(String itemName, int count, int orderPrice, String savePath, List<ItemImageDto> itemImageDtos){
        return new OrderItemDto(itemName, count, orderPrice, savePath, itemImageDtos);
    }

    public static OrderItemDto from(OrderItem entity){

        List<ItemImageDto> itemImageDtos = entity.getItem().getItemimages().stream()
                .map(ItemImageDto::from)
                .collect(Collectors.toList());

        String savePath = null;
        if (!itemImageDtos.isEmpty()) {
            savePath = itemImageDtos.get(0).storedFileName();
        }

        return new OrderItemDto(
                entity.getItem().getItemName(),
                entity.getCount(),
                entity.getItem().getPrice(),
                savePath,
                itemImageDtos
        );
    }
}
