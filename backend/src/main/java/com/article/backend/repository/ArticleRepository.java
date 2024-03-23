package com.article.backend.repository;

import com.article.backend.model.Article;
import com.article.backend.model.enums.ArticleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {
    Optional<Article> findArticleByIdAndStatus(Long id, ArticleStatus status);

    List<Article> findAllByStatus(ArticleStatus status);
}
