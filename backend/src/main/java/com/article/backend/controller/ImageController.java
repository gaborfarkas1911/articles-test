package com.article.backend.controller;

import com.article.backend.exception.FileStorageException;
import com.article.backend.model.Article;
import com.article.backend.model.ArticleImage;
import com.article.backend.service.ArticleImageService;
import com.article.backend.service.ArticleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

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

        try {
            ArticleImage newArticleImage = new ArticleImage();
            newArticleImage.setArticle(article);
            newArticleImage.setName(file.getOriginalFilename());
            newArticleImage.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
            return ResponseEntity.ok(articleImageService.saveArticleImage(newArticleImage));
        } catch (IOException e) {
            throw new FileStorageException("Could not store the file. Please try again!");
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImage(@RequestParam Long id) {
        articleImageService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
