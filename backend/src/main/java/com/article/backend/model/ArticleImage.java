package com.article.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "article_image")
public class ArticleImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Column
    private String name;

    @NotBlank(message = "Image is mandatory")
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB") //max: 16MB, or we can use LONGBLOB(4GB) instead
    private String image;
}
