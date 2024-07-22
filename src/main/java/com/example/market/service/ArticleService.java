package com.example.market.service;

import com.example.market.domain.Article;
import com.example.market.domain.ArticleFile;
import com.example.market.domain.UserAccount;
import com.example.market.domain.type.SearchType;
import com.example.market.dto.ArticleDto;
import com.example.market.dto.ArticleWithCommentsDto;
import com.example.market.repository.ArticleFileRepository;
import com.example.market.repository.ArticleRepository;
import com.example.market.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;
    private final FileService fileService;
    private final ArticleFileRepository articleFileRepository;

    // 상세보기
    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    // 수정 - get
    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    // 수정 - post
    public void updateArticle(Long articleId, ArticleDto dto) throws IOException {
        try {
            Article article = articleRepository.getReferenceById(articleId);
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

            if (article.getUserAccount().equals(userAccount)) {
                if (dto.title() != null) { article.setTitle(dto.title()); }
                if (dto.content() != null) { article.setContent(dto.content()); }
                article.setHashtag(dto.hashtag());

                // 첨부파일 처리
                if (dto.imageFiles() != null && !dto.imageFiles().isEmpty()) {
                    // 기존 파일 삭제
                    List<ArticleFile> existingFiles = articleFileRepository.findByArticleId(articleId);
                    for (ArticleFile existingFile : existingFiles) {
                        fileService.deleteFile(existingFile.getSavePath());
                    }
                    articleFileRepository.deleteAll(existingFiles);

                    // 새 파일 저장
                    List<ArticleFile> articleFiles = storeFiles(dto.imageFiles(), article);
                    articleFileRepository.saveAll(articleFiles);
                }

            }
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    // 저장
    public void saveArticle(ArticleDto dto) throws IOException  {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

        Article article = dto.toEntity(userAccount);
        articleRepository.save(article);

        List<ArticleFile> articleFiles = storeFiles(dto.imageFiles(), article);
        articleFileRepository.saveAll(articleFiles);

    }

    // 조회, 검색 조건에 따라 게시글 검색
    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {

        if (searchKeyword == null || searchKeyword.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_UserNameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
        };
    }

    // 해시태그 통해서 검색
    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(String hashtag, Pageable pageable){
        if(hashtag == null || hashtag.isBlank()){
            return Page.empty(pageable);
        }
        return articleRepository.findByHashtag(hashtag, pageable).map(ArticleDto::from);
    }

    public List<String> getHashtags() {
        return articleRepository.findAllDistinctHashtags();
    }

    public long getArticleCount() {
        return articleRepository.count();
    }

    // 삭제
    public void deleteArticle(long articleId, String userId) {
        articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
    }


    // 파일 추가
    public List<ArticleFile> storeFiles(List<MultipartFile> multipartFiles, Article article) throws IOException {
        List<ArticleFile> articleFiles = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                String storedFileName = fileService.storeFile(multipartFile);
                String originalFilename = multipartFile.getOriginalFilename();
                String fullPath = fileService.getFullPath(storedFileName);

                ArticleFile articleFile = ArticleFile.of(originalFilename, storedFileName, fullPath, article);
                articleFiles.add(articleFile);
            }
        }
        return articleFiles;
    }

    /** 내가 작성한 글 보기 */
    @Transactional(readOnly = true)
    public Page<ArticleDto> findArticlesByUserId(String userId, Pageable pageable) {
        return articleRepository.findAllByUserAccount_UserId(userId, pageable)
                .map(ArticleDto::from);
    }

}
