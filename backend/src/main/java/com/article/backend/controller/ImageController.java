package com.article.backend.controller;

import com.article.backend.model.Article;
import com.article.backend.model.ArticleImage;
import com.article.backend.service.ArticleImageService;
import com.article.backend.service.ArticleService;
import com.article.backend.util.FileToBase64Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    ArticleImageService articleImageService;

    @Autowired
    ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleImage> saveImage(@RequestParam Long articleId,
                                                  @RequestPart MultipartFile file) {
        Article article = articleService.getArticleById(articleId);
        if (article == null) {
            throw new EntityNotFoundException("Article not found.");
        }
        ArticleImage newArticleImage = new ArticleImage();
        newArticleImage.setArticle(article);
        newArticleImage.setName(file.getOriginalFilename());
        newArticleImage.setImage(FileToBase64Utils.getBase64ImageFromFile(file));
        return ResponseEntity.ok(articleImageService.saveArticleImage(newArticleImage));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImage(@RequestParam Long id) {
        ArticleImage existingArticleImage = articleImageService.getArticleImageById(id);
        if (existingArticleImage == null) {
            throw new EntityNotFoundException("ArticleImage not found.");
        }
        articleImageService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
