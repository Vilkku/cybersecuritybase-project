DROP TABLE IF EXISTS Comment;

CREATE TABLE Comment (
    id int(11) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    content text(1000)
);
