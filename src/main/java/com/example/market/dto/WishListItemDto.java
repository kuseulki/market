package com.example.market.dto;

import com.example.market.domain.Item;
import com.example.market.domain.WishList;
import com.example.market.domain.WishListItem;

public record WishListItemDto (

        Long id,
        Long wishListId,
        Long itemId,
        Integer count
){

    public static WishListItemDto of(Long cartId, Long itemId, Integer count){
        return WishListItemDto.of(null, cartId, itemId, count);
    }

    public static WishListItemDto of(Long id, Long cartId, Long itemId, Integer count){
        return new WishListItemDto(id, cartId, itemId, count);
    }

    public static WishListItemDto from(WishListItem entity){
        return new WishListItemDto(
                entity.getId(),
                entity.getWishList().getId(),
                entity.getItem().getId(),
                entity.getCount()
        );
    }

    public WishListItem toEntity(WishList wishList, Item item) {
        return WishListItem.of(
                wishList,
                item,
                count
        );
    }
}
