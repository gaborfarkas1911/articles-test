package com.article.backend.repository;

import com.article.backend.model.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
    Optional<ArticleTag> findFirstByTagAndArticle_Id(String tag, Long articleId);
}
