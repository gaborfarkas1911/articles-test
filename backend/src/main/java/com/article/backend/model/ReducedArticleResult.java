package com.article.backend.model;

import com.article.backend.model.enums.ArticleCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReducedArticleResult {
    private Long id;
    private String title;
    private String subTitle;
    private ArticleCategory category;
    private ArticleTag tag;
}
