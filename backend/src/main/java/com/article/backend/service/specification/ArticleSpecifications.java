package com.article.backend.service.specification;

import com.article.backend.model.Article;
import com.article.backend.model.ArticleTag;
import com.article.backend.model.enums.ArticleCategory;
import com.article.backend.model.enums.ArticleStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ArticleSpecifications {

    private ArticleSpecifications() {}

    public static Specification<Article> hasTitle(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("title"), title);
    }

    public static Specification<Article> hasStatus(ArticleStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Article> hasCategory(ArticleCategory category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Article> hasTag(String tag) {
        return (root, query, criteriaBuilder) -> {
            Join<Article, ArticleTag> tagJoin = root.join("tags", JoinType.INNER);
            return criteriaBuilder.equal(tagJoin.get("tag"), tag);
        };
    }
}