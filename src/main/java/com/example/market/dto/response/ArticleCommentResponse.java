package com.example.market.dto.response;

import com.example.market.dto.ArticleCommentDto;
import java.time.LocalDateTime;
public record ArticleCommentResponse(
        Long id,
        String content,
        String email,
        String userName,
        String userId,
        LocalDateTime createdAt
){

    public static ArticleCommentResponse of(Long id, String content, String email, String userName, String userId, LocalDateTime createdAt) {
        return new ArticleCommentResponse(id, content, email, userName, userId, createdAt);
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto) {
        String userName = dto.userAccountDto().userName();
        if (userName == null || userName.isBlank()) {
            userName = dto.userAccountDto().userId();
        }

        return new ArticleCommentResponse(
                dto.id(),
                dto.content(),
                dto.userAccountDto().email(),
                userName,
                dto.userAccountDto().userId(),
                dto.createdAt()
        );
    }

}