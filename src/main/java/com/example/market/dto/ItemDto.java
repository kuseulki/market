package com.example.market.dto;

import com.example.market.domain.Item;
import com.example.market.domain.UserAccount;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ItemDto(
        Long id,
        UserAccountDto userAccountDto,
        String itemName,        // 상품명
        Integer price,              // 가격
        Integer stockNumber,        // 재고 수량
        String itemDetail,      // 상품 설명
        String hashtag,

        List<MultipartFile> imageFiles,    // save.html -> Controller 파일 담는 용도
        List<ItemImageDto> itemImageDtos, // 파일 정보

        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static ItemDto of(UserAccountDto userAccountDto, String itemName, Integer price, Integer stockNumber, String itemDetail, String hashtag, List<MultipartFile> imageFiles, List<ItemImageDto> itemImageDtos) {
        return new ItemDto(null, userAccountDto, itemName, price, stockNumber, itemDetail, hashtag, imageFiles, itemImageDtos, null, null,null,null);
    }

    public static ItemDto of(UserAccountDto userAccountDto, Long id, String itemName, Integer price, Integer stockNumber, String itemDetail, String hashtag, List<MultipartFile> imageFiles, List<ItemImageDto> itemImageDtos, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ItemDto(id, userAccountDto, itemName, price, stockNumber, itemDetail, hashtag, imageFiles, itemImageDtos, createdAt, createdBy,modifiedAt,modifiedBy);
    }

    public static ItemDto from(Item entity) {
        return new ItemDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getItemName(),
                entity.getPrice(),
                entity.getStockNumber(),
                entity.getItemDetail(),
                entity.getHashtag(),
                null,
                entity.getItemimages().stream().map(ItemImageDto::from).collect(Collectors.toList()),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public Item toEntity(UserAccount userAccount) {
        return Item.of(
                userAccount,
                itemName,
                price,
                stockNumber,
                itemDetail,
                hashtag
        );
    }

}