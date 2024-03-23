CREATE TABLE article_tag (
	id int AUTO_INCREMENT PRIMARY KEY,
	article_id int NOT NULL,
	tag varchar(100) NOT NULL,
	CONSTRAINT article_tag_article_FK FOREIGN KEY (article_id) REFERENCES article(id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_hungarian_ci;
