package com.example.market.dto;

import com.example.market.domain.WishListItem;

import java.util.List;
import java.util.stream.Collectors;

public record WishListDetailDto(

        Long id,
        Long wishListItemId,
        String itemName,
        Integer price,
        Integer count,
        List<ItemImageDto> itemImageDtos, //  이미지
        Long itemId
) {
    public static WishListDetailDto of(Long id, Long wishListItemId, String itemName, Integer price, Integer count, List<ItemImageDto> itemImageDtos, Long itemId) {
        return new WishListDetailDto(id, wishListItemId, itemName, price, count, itemImageDtos, itemId);
    }

    public static WishListDetailDto from(WishListItem entity){

        List<ItemImageDto> itemImageDtos = entity.getItem().getItemimages().stream()
                .map(ItemImageDto::from)
                .collect(Collectors.toList());

        return new WishListDetailDto(
                entity.getId(),
                entity.getId(),
                entity.getItem().getItemName(),
                entity.getItem().getPrice(),
                entity.getCount(),
                itemImageDtos,
                entity.getItem().getId()
        );
    }
}