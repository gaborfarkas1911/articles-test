package com.article.backend.service;

import com.article.backend.model.Article;
import com.article.backend.model.enums.ArticleCategory;
import com.article.backend.model.enums.ArticleStatus;

import java.util.List;

public interface ArticleService {
    Article saveArticle(Article article);

    List<Article> findAll(String title, ArticleCategory category, ArticleStatus status, String tag);

    Article getArticleById(Long id);

    Article getArticleByIdAndStatus(Long id, ArticleStatus status);

    List<Article> listArticles();

    List<Article> listArticlesByStatus(ArticleStatus statusStatus);

    void deleteById(Long id);
}
