package com.example.market.dto.response;

import com.example.market.dto.ItemDto;
import com.example.market.dto.ItemImageDto;
import java.time.LocalDateTime;
import java.util.List;

public record ItemResponse(

        Long id,
        String itemName,        // 상품명
        Integer price,              // 가격
        Integer stockNumber,        // 재고 수량
        String itemDetail,      // 상품 설명
        String hashtag,
        String userName,
        String userId,
        List<ItemImageDto> itemImageDtos, //  이미지
        LocalDateTime createdAt
) {
    public static ItemResponse of(Long id, String itemName, int price, int stockNumber, String itemDetail, String hashtag, String userName, String userId, List<ItemImageDto> itemImageDtos, LocalDateTime createdAt) {
        return new ItemResponse(id, itemName, price, stockNumber, itemDetail, hashtag, userName, userId, itemImageDtos, createdAt);
    }

    public static ItemResponse from(ItemDto dto){
        String userName = dto.userAccountDto().userName();
        if (userName == null || userName.isBlank()) {
            userName = dto.userAccountDto().userId();
        }

        return new ItemResponse(
                dto.id(),
                dto.itemName(),
                dto.price(),
                dto.stockNumber(),
                dto.itemDetail(),
                dto.hashtag(),
                userName,
                dto.userAccountDto().userId(),
                dto.itemImageDtos(),
                dto.createdAt()
        );

    }
}
