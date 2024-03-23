package com.article.backend.controller;

import com.article.backend.model.Article;
import com.article.backend.model.ArticleTag;
import com.article.backend.model.ReducedArticleResult;
import com.article.backend.model.enums.ArticleCategory;
import com.article.backend.model.enums.ArticleStatus;
import com.article.backend.service.ArticleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PreAuthorize("hasRole('JOURNALIST')")
    @PostMapping
    public ResponseEntity<Article> saveArticle(@RequestBody @Valid Article article) {
        if (article.getId() != null) {
            throw new ValidationException("This request cannot contain the id.");
        }
        if (article.getStatus() == null || !article.getStatus().equals(ArticleStatus.AWAITING_APPROVAL)) {
            article.setStatus(ArticleStatus.AWAITING_APPROVAL);
        }
        return ResponseEntity.ok(articleService.saveArticle(article));
    }

    @PreAuthorize("hasRole('JOURNALIST')")
    @PutMapping
    public ResponseEntity<Article> updateArticle(@RequestBody @Valid Article article) {
        if (article.getId() == null) {
            throw new ValidationException("Id is required.");
        }

        Article existingArticle = articleService.getArticleById(article.getId());
        if (existingArticle == null) {
            throw new EntityNotFoundException("Article not found.");
        }
        existingArticle.setCategory(article.getCategory());
        existingArticle.setTitle(article.getTitle());
        existingArticle.setSubTitle(article.getSubTitle());
        existingArticle.setContent(article.getContent());
        return ResponseEntity.ok(articleService.saveArticle(article));
    }

    @PreAuthorize("hasRole('EDITOR_IN_CHIEF')")
    @PutMapping("/update-status")
    public ResponseEntity<Article> updateStatus(@RequestParam Long id,
                                                @RequestParam ArticleStatus status) {
        Article existingArticle = articleService.getArticleById(id);
        if (existingArticle == null) {
            throw new EntityNotFoundException("Article not found.");
        }
        existingArticle.setStatus(status);
        return ResponseEntity.ok(articleService.saveArticle(existingArticle));
    }

    @PreAuthorize("hasRole('EDITOR_IN_CHIEF')")
    @GetMapping
    public ResponseEntity<Article> getArticle(@RequestParam Long id) {
        return ResponseEntity.ok(articleService.getArticleById(id));
    }

    @GetMapping("/approved-article")
    public ResponseEntity<Article> getApprovedArticle(@RequestParam Long id) {
        return ResponseEntity.ok(articleService.getArticleByIdAndStatus(id, ArticleStatus.APPROVED));
    }

    @GetMapping("/all-approved")
    public ResponseEntity<List<Article>> listApprovedArticles() {
        return ResponseEntity.ok(articleService.listArticlesByStatus(ArticleStatus.APPROVED));
    }

    @PreAuthorize("hasRole('EDITOR_IN_CHIEF')")
    @GetMapping("/all")
    public ResponseEntity<List<Article>> listArticles(@RequestParam(required = false) ArticleStatus status) {
        return ResponseEntity.ok(status != null ? articleService.listArticlesByStatus(status) : articleService.listArticles());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReducedArticleResult>> search(@RequestParam(required = false) ArticleCategory category,
                                                             @RequestParam(required = false) String title,
                                                             @RequestParam(required = false) String tag) {
        String requestedTag = null;
        if (StringUtils.hasText(tag)) {
            if (!tag.startsWith("#")) {
                throw new ValidationException("Incorrect tag format, the correct format is #<TAG>.");
            }
            requestedTag = org.apache.commons.lang3.StringUtils.deleteWhitespace(tag);
        }

        List<Article> articles = articleService.findAll(title, category, ArticleStatus.APPROVED, requestedTag);

        if (CollectionUtils.isEmpty(articles)) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(articles.stream().map(article -> {
            ArticleTag existingTag = StringUtils.hasText(tag) && !CollectionUtils.isEmpty(article.getTags()) ?
                    article.getTags()
                            .stream()
                            .filter(articleTag -> articleTag.getTag().equals(tag))
                            .findFirst()
                            .orElse(null) : null;
            return new ReducedArticleResult(article.getId(), article.getTitle(), article.getSubTitle(), article.getCategory(), existingTag);
        }).toList());
    }
}
