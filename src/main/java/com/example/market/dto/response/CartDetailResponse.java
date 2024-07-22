package com.example.market.dto.response;

import com.example.market.dto.CartDetailDto;

public record CartDetailResponse(
        Long cartItemId,
        String itemName,
        Integer price,
        Integer count,
        String savePath
) {

    public static CartDetailResponse of(Long cartItemId, String itemName, Integer price, Integer count, String savePath) {
        return new CartDetailResponse(cartItemId ,itemName, price,count, savePath);
    }

    public static CartDetailResponse from(CartDetailDto dto) {

        String firstImagePath = null;
        if (dto.itemImageDtos() != null && !dto.itemImageDtos().isEmpty()) {
            firstImagePath = dto.itemImageDtos().get(0).storedFileName();
        }

        return new CartDetailResponse(
                dto.cartItemId(),
                dto.itemName(),
                dto.price(),
                dto.count(),
                firstImagePath
        );
    }
}
