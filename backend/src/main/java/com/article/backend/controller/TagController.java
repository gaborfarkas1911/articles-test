package com.article.backend.controller;

import com.article.backend.exception.AlreadyExistsException;
import com.article.backend.model.Article;
import com.article.backend.model.ArticleTag;
import com.article.backend.service.ArticleService;
import com.article.backend.service.ArticleTagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    ArticleTagService articleTagService;

    @Autowired
    ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleTag> saveTag(@RequestParam Long articleId,
                                              @RequestParam String tag) {
        if (!StringUtils.hasText(tag) || !tag.startsWith("#")) {
            throw new ValidationException("Incorrect tag format, the correct format is #<TAG>.");
        }

        String formattedTag = org.apache.commons.lang3.StringUtils.deleteWhitespace(tag);

        Article article = articleService.getArticleById(articleId);
        if (article == null) {
            throw new EntityNotFoundException("Article not found.");
        }

        ArticleTag existingArticleTag = articleTagService.getByTagAndArticle(formattedTag, articleId);
        if (existingArticleTag != null) {
            throw new AlreadyExistsException("This tag already exists for the given article.");
        }

        ArticleTag newArticleTag = new ArticleTag();
        newArticleTag.setArticle(article);
        newArticleTag.setTag(formattedTag);
        return ResponseEntity.ok(articleTagService.saveArticleTag(newArticleTag));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTag(@RequestParam Integer id){
        articleTagService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
