package com.online.library.app.repository;

import com.online.library.app.model.User;
import com.online.library.app.model.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
