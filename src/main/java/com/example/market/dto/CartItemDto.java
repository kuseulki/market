package com.example.market.dto;

import com.example.market.domain.Cart;
import com.example.market.domain.CartItem;
import com.example.market.domain.Item;

public record CartItemDto(
        Long id,
        Long cartId,
        Long itemId,
        Integer count

) {
    public static CartItemDto of(Long cartId, Long itemId, Integer count){
        return new CartItemDto(null, cartId, itemId, count);
    }

    public static CartItemDto of(Long id, Long cartId, Long itemId, Integer count){
        return new CartItemDto(id, cartId, itemId, count);
    }

    public static CartItemDto from(CartItem entity){
        return new CartItemDto(
                entity.getId(),
                entity.getCart().getId(),
                entity.getItem().getId(),
                entity.getCount()
        );
    }

    public CartItem toEntity(Cart cart, Item item) {
        return CartItem.of(
                cart,
                item,
                count
        );
    }

}
