CREATE TABLE article_image (
	id int AUTO_INCREMENT PRIMARY KEY,
	article_id int NOT NULL,
	name varchar(100) NOT NULL,
	image MEDIUMBLOB NOT NULL,
	CONSTRAINT article_image_article_FK FOREIGN KEY (article_id) REFERENCES article(id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_hungarian_ci;