package com.article.backend.service;

import com.article.backend.model.ArticleImage;

public interface ArticleImageService {
    ArticleImage saveArticleImage(ArticleImage articleImage);

    void deleteById(Long id);
}
