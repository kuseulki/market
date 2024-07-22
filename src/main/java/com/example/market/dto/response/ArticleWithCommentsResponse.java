package com.example.market.dto.response;

import com.example.market.dto.ArticleFileDto;
import com.example.market.dto.ArticleWithCommentsDto;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentsResponse(
        Long id,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String email,
        String userName,
        String userId,
        Set<ArticleCommentResponse> articleCommentsResponse,
        List<ArticleFileDto> articleFileDtos // 파일 정보
) {
    public static ArticleWithCommentsResponse of(Long id, String title, String content, String hashtag, LocalDateTime createdAt, String email, String userName, String userId, Set<ArticleCommentResponse> articleCommentResponses, List<ArticleFileDto> articleFileDtos) {
        return new ArticleWithCommentsResponse(id, title, content, hashtag, createdAt, email, userName, userId, articleCommentResponses, articleFileDtos);
    }

    public static ArticleWithCommentsResponse from(ArticleWithCommentsDto dto) {
        String userName = dto.userAccountDto().userName();
        if (userName == null || userName.isBlank()) {
            userName = dto.userAccountDto().userId();
        }

        return new ArticleWithCommentsResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtag(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                userName,
                dto.userAccountDto().userId(),
                dto.articleCommentDtos().stream()
                        .map(ArticleCommentResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                dto.articleFileDtos()
        );
    }

}