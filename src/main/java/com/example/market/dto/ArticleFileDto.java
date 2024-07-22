package com.example.market.dto;

import com.example.market.domain.Article;
import com.example.market.domain.ArticleFile;

public record ArticleFileDto(
        Long id,
        Long boardId,
        String originalFileName,
        String storedFileName,
        String savePath
) {

    public static ArticleFileDto of(String originalFileName, String storedFileName, String savePath){
        return new ArticleFileDto(null,null, originalFileName, storedFileName, savePath);
    }

    public static ArticleFileDto of(Long id, Long boardId, String originalFileName, String storedFileName, String savePath){
        return new ArticleFileDto(id, boardId, originalFileName, storedFileName, savePath);
    }

    public static ArticleFileDto from(ArticleFile entity){
        return new ArticleFileDto(
                entity.getId(),
                entity.getArticle().getId(),
                entity.getOriginalFileName(),
                entity.getStoredFileName(),
                entity.getSavePath()
        );
    }

    public ArticleFile toEntity(Article article){
        return ArticleFile.of(
                originalFileName,
                storedFileName,
                savePath,
                article
        );
    }
}
