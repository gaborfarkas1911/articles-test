CREATE TABLE article (
	id int AUTO_INCREMENT PRIMARY KEY,
	title varchar(200) NOT NULL,
	sub_title varchar(200) NOT NULL,
	category varchar(10) NOT NULL,
	content TEXT NOT NULL,
	status varchar(17) NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_hungarian_ci;