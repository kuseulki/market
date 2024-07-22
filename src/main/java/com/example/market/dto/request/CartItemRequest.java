package com.example.market.dto.request;

import com.example.market.dto.CartItemDto;
public record CartItemRequest(

        Long cartId,
        Long itemId,
        Integer count
) {
    public static CartItemRequest of(Long cartId, Long itemId, Integer count) {
        return new CartItemRequest(cartId, itemId, count);
    }
    public CartItemDto toDto(){
        return CartItemDto.of(
                cartId,
                itemId,
                count
        );
    }
}
