CREATE TABLE borrow
(
  book_id VARCHAR(45) NOT NULL,
  PRIMARY KEY (book_id),
  user_id BIGINT(20) NOT NULL
);

ALTER TABLE borrow
ADD CONSTRAINT FK_Book_User FOREIGN KEY (user_id)
REFERENCES user (id);
