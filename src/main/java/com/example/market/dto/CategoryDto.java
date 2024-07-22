package com.example.market.dto;

import com.example.market.domain.Category;

public record CategoryDto(
        Long categoryId,
        String categoryName
) {

    public static CategoryDto of(){
        return new CategoryDto(null, null);
    }

    public static CategoryDto of(Long categoryId, String categoryName) {
        return new CategoryDto(categoryId, categoryName);
    }

    public static CategoryDto from(Category entity){
        return new CategoryDto(
                entity.getId(),
                entity.getName()
        );
    }

    public Category toEntity(){
        return Category.of(
                categoryName
        );
    }
}
