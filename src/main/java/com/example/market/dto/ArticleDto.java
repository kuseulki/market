package com.example.market.dto;

import com.example.market.domain.Article;
import com.example.market.domain.UserAccount;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ArticleDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy,

        // 파일 관련
        List<MultipartFile> imageFiles,    // save.html -> Controller 파일 담는 용도
        List<ArticleFileDto> articleFileDtos // 파일 정보
) {
    public static ArticleDto of(UserAccountDto userAccountDto, String title, String content, String hashtag, List<MultipartFile> imageFiles, List<ArticleFileDto> articleFileDtos) {
        return new ArticleDto(null, userAccountDto, title, content, hashtag, null, null, null, null, imageFiles, articleFileDtos);
    }

    public static ArticleDto of(Long id, UserAccountDto userAccountDto, String title, String content, String hashtag, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy, List<MultipartFile> imageFiles, List<ArticleFileDto> articleFileDtos) {
        return new ArticleDto(id, userAccountDto, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy, null, null);
    }

    public static ArticleDto from(Article entity) {
        return new ArticleDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtag(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy(),
                null,
                entity.getImageFiles().stream().map(ArticleFileDto::from).collect(Collectors.toList())
        );
    }

    public Article toEntity(UserAccount userAccount) {
        return Article.of(
                userAccount,
                title,
                content,
                hashtag
        );
    }

}