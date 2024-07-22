package com.example.market.repository;

import com.example.market.domain.ArticleFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleFileRepository extends JpaRepository<ArticleFile, Long> {

    List<ArticleFile> findByArticleId(Long articleId);
}
