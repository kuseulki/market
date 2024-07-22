package com.example.market.dto;

import com.example.market.domain.Item;
import com.example.market.domain.ItemImage;

public record ItemImageDto(
        Long id,
        Long itemId,
        String originalFileName,
        String storedFileName,
        String savePath,
        Boolean imageYn
) {

    public static ItemImageDto of(String originalFileName, String storedFileName, String savePath, Boolean imageYn){
        return new ItemImageDto(null,null, originalFileName, storedFileName, savePath, imageYn);
    }

    public static ItemImageDto of(Long id, Long itemId, String originalFileName, String storedFileName, String savePath, Boolean imageYn){
        return new ItemImageDto(id, itemId, originalFileName, storedFileName, savePath, imageYn);
    }

    public static ItemImageDto from(ItemImage entity){
        return new ItemImageDto(
                entity.getId(),
                entity.getItem().getId(),
                entity.getOriginalFileName(),
                entity.getStoredFileName(),
                entity.getSavePath(),
                entity.getImageYn()
        );
    }

    public ItemImage toEntity(Item item){
        return ItemImage.of(
                originalFileName,
                storedFileName,
                savePath,
                item
        );
    }
}
