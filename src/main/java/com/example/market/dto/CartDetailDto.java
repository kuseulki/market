package com.example.market.dto;

import com.example.market.domain.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public record CartDetailDto(
        Long cartItemId,
        String itemName,
        Integer price,
        Integer count,
        List<ItemImageDto> itemImageDtos //  이미지

) {

    public static CartDetailDto of(Long cartItemId, String itemName, Integer price, Integer count, List<ItemImageDto> itemImageDtos){
        return new CartDetailDto(cartItemId, itemName, price, count, itemImageDtos);
    }

    public static CartDetailDto from(CartItem entity){

        List<ItemImageDto> itemImageDtos = entity.getItem().getItemimages().stream()
                .map(ItemImageDto::from)
                .collect(Collectors.toList());

        return new CartDetailDto(
                entity.getId(),
                entity.getItem().getItemName(),
                entity.getItem().getPrice(),
                entity.getCount(),
                itemImageDtos
        );
    }

}
