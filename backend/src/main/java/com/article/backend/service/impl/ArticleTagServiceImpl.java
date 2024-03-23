package com.article.backend.service.impl;

import com.article.backend.model.ArticleTag;
import com.article.backend.repository.ArticleTagRepository;
import com.article.backend.service.ArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleTagServiceImpl implements ArticleTagService {

    @Autowired
    private ArticleTagRepository repository;

    @Override
    public ArticleTag saveArticleTag(ArticleTag articleTag) {
        return repository.save(articleTag);
    }

    @Override
    public ArticleTag getByTagAndArticle(String tag, Long articleId) {
        return repository.findFirstByTagAndArticle_Id(tag, articleId).orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
