package com.online.library.app.service.interfaces;

import com.online.library.app.dto.request.BookRequest;
import com.online.library.app.dto.response.BookResponse;

import java.util.List;

public interface BookServiceInterface {
    void createBook(BookRequest request);

    void deleteBook(Long id);

    List<BookResponse> getAllBooks(String sortKey, String mode);

    BookResponse getBookById(Long id);

    void editBookInfo(BookRequest request, Long id);

    void addBookToCollection(Long collectionId, Long bookId);

    void removeBookFromCollection(Long collectionId, Long bookId);
}
