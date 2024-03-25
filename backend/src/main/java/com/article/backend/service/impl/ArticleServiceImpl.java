package com.article.backend.service.impl;

import com.article.backend.model.Article;
import com.article.backend.model.enums.ArticleCategory;
import com.article.backend.model.enums.ArticleStatus;
import com.article.backend.repository.ArticleRepository;
import com.article.backend.service.ArticleService;
import com.article.backend.service.specification.ArticleSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository repository;

    @Override
    public Article saveArticle(Article article) {
        return repository.save(article);
    }

    @Override
    public List<Article> findAll(String title, ArticleCategory category, ArticleStatus status, String tag) {
        Specification<Article> spec = Specification.where(null);

        if (StringUtils.hasText(title)) {
            spec = spec.and(ArticleSpecifications.hasTitle(title));
        }

        if (StringUtils.hasText(tag)) {
            spec = spec.and(ArticleSpecifications.hasTag(tag));
        }

        if (status != null) {
            spec = spec.and(ArticleSpecifications.hasStatus(status));
        }

        if (category != null) {
            spec = spec.and(ArticleSpecifications.hasCategory(category));
        }

        return repository.findAll(spec);
    }

    @Override
    public Article getArticleById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Article getArticleByIdAndStatus(Long id, ArticleStatus status) {
        return repository.findArticleByIdAndStatus(id, status).orElse(null);
    }

    @Override
    public List<Article> listArticles() {
        return repository.findAll();
    }

    @Override
    public List<Article> listArticlesByStatus(ArticleStatus statusStatus) {
        return repository.findAllByStatus(statusStatus);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
