package com.example.market.dto.request;

import com.example.market.dto.ItemDto;
import com.example.market.dto.UserAccountDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ItemRequest(
        @NotBlank(message = "상품명은 필수 입력 값입니다.")
        String itemName,        // 상품명
        @NotNull(message = "가격은 필수 입력 값입니다.")
        Integer price,              // 가격
        @NotNull(message = "재고는 필수 입력 값입니다.")
        Integer stockNumber,        // 재고 수량
        @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
        String itemDetail,      // 상품 설명
        String hashtag,
        List<MultipartFile> itemimages // 파일 관련
) {

    public static ItemRequest of(){
        return new ItemRequest(null, null, null, null, null, null);
    }
    public static ItemRequest of(String itemName, int price, int stockNumber, String itemDetail,  String hashtag, List<MultipartFile> itemimages){
        return new ItemRequest(itemName, price, stockNumber, itemDetail, hashtag, itemimages);
    }

    public ItemDto toDto(UserAccountDto userAccountDto){
        return ItemDto.of(
                userAccountDto,
                itemName,
                price,
                stockNumber,
                itemDetail,
                hashtag,
                itemimages,
                null
        );
    }
}
