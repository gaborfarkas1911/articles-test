package com.article.backend.service.impl;

import com.article.backend.model.ArticleImage;
import com.article.backend.repository.ArticleImageRepository;
import com.article.backend.service.ArticleImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleImageServiceImpl implements ArticleImageService {

    @Autowired
    private ArticleImageRepository repository;

    @Override
    public ArticleImage saveArticleImage(ArticleImage articleImage) {
        return repository.save(articleImage);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
