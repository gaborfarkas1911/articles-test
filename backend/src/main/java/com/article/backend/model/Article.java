package com.article.backend.model;

import com.article.backend.model.enums.ArticleCategory;
import com.article.backend.model.enums.ArticleStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Column
    private String title;

    @NotBlank(message = "SubTitle is mandatory")
    @Column(name = "sub_title")
    private String subTitle;

    @NotBlank(message = "Content is mandatory")
    @Column
    private String content;

    @NotNull(message = "Category is mandatory")
    @Column
    @Enumerated(EnumType.STRING)
    private ArticleCategory category;

    @Column
    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    @JsonManagedReference
    @OneToMany(mappedBy = "article",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ArticleImage> images = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "article",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ArticleTag> tags = new ArrayList<>();
}