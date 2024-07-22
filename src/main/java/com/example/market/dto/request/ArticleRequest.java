package com.example.market.dto.request;

import com.example.market.dto.ArticleDto;
import com.example.market.dto.UserAccountDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ArticleRequest(
        String title,
        String content,
        String hashtag,

        List<MultipartFile> imageFiles // 파일 관련
) {

    public static ArticleRequest of(String title, String content, String hashtag, List<MultipartFile> imageFiles) {
        return new ArticleRequest(title, content, hashtag, imageFiles);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return ArticleDto.of(
                userAccountDto,
                title,
                content,
                hashtag,
                imageFiles,
                null
        );
    }

}
