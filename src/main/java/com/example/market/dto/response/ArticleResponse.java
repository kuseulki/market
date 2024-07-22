package com.example.market.dto.response;

import com.example.market.dto.ArticleDto;
import com.example.market.dto.ArticleFileDto;
import java.time.LocalDateTime;
import java.util.List;

public record ArticleResponse(
        Long id,
        String title,
        String content,
        String hashtag,
        String email,
        String userName,
        List<ArticleFileDto> articleFileDtos,    // 파일 정보
        LocalDateTime createdAt
) {
    public static ArticleResponse of(Long id, String title, String content, String hashtag, String email, String userName, List<ArticleFileDto> articleFileDtos, LocalDateTime createdAt) {
        return new ArticleResponse(id, title, content, hashtag, email, userName, articleFileDtos, createdAt);
    }

    public static ArticleResponse from(ArticleDto dto) {
        String userName = dto.userAccountDto().userName();
        if (userName == null || userName.isBlank()) {
            userName = dto.userAccountDto().userId();
        }

        return new ArticleResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtag(),
                dto.userAccountDto().email(),
                userName,
                dto.articleFileDtos(),
                dto.createdAt()
        );
    }

}
