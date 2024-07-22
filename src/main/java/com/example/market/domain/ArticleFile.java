package com.example.market.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Table
@Getter
@Setter
@Entity
public class ArticleFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_file_id")
    private Long id;

    @Column private String originalFileName;
    @Column private String storedFileName;
    @Column private String savePath;
    @ManyToOne @JoinColumn(name = "article_id")
    private Article article;

    protected ArticleFile(){}

    public ArticleFile(String originalFileName, String storedFileName, String savePath, Article article) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.savePath = savePath;
        this.article = article;
    }

    public static ArticleFile of(String originalFileName, String storedFileName,  String savePath, Article article){
        return new ArticleFile(originalFileName, storedFileName, savePath, article);
    }
}
