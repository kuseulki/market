package com.example.market.dto.response;

import com.example.market.dto.WishListDetailDto;

public record WishListDetailResponse(
    Long wishListItemId,
    String itemName,
    Integer price,
    Integer count,
    String savePath, //  이미지
    Long itemId
) {
        public static WishListDetailResponse of(Long wishListItemId, String itemName, Integer price, Integer count, String savePath, Long itemId){
            return new WishListDetailResponse(wishListItemId, itemName, price, count, savePath, itemId);
        }

        public static WishListDetailResponse from(WishListDetailDto dto){

            String firstImagePath = null;
            if (dto.itemImageDtos() != null && !dto.itemImageDtos().isEmpty()) {
                firstImagePath = dto.itemImageDtos().get(0).storedFileName();
            }
            return new WishListDetailResponse(
                    dto.wishListItemId(),
                    dto.itemName(),
                    dto.price(),
                    dto.count(),
                    firstImagePath,
                    dto.itemId()
            );
        }
    }
