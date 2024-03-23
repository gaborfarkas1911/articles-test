package com.article.backend.service;

import com.article.backend.model.ArticleTag;

public interface ArticleTagService {
    ArticleTag saveArticleTag(ArticleTag articleTag);

    ArticleTag getByTagAndArticle(String tag, Long articleId);

    void deleteById(Integer id);
}
