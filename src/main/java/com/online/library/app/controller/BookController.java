package com.online.library.app.controller;

import com.online.library.app.dto.request.BookRequest;
import com.online.library.app.dto.response.BookResponse;
import com.online.library.app.dto.response.CollectionResponse;
import com.online.library.app.exceptionhandler.ErrorResponse;
import com.online.library.app.exceptionhandler.book.BookDeletionException;
import com.online.library.app.exceptionhandler.book.BookNotFoundException;
import com.online.library.app.exceptionhandler.book.BookUpdateException;
import com.online.library.app.service.interfaces.BookServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class BookController {

    private final BookServiceInterface bookService;

    @PostMapping("/api/add-book/{bookId}/to-collection/{collectionId}")
    public ResponseEntity<CollectionResponse> addBookToCollection(
            @PathVariable Long bookId,
            @PathVariable Long collectionId) {
        bookService.addBookToCollection(collectionId, bookId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/api/remove-book/{bookId}/from-collection/{collectionId}")
    public ResponseEntity<CollectionResponse> removeBookFromCollection(
            @PathVariable Long collectionId,
            @PathVariable Long bookId) {
        bookService.removeBookFromCollection(collectionId, bookId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/api/create-book")
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest request) {
        bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/api/delete-book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/book/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/api/books")
    public ResponseEntity<List<BookResponse>> getBooks(
            @RequestParam(defaultValue = "publicationDate") String key,
            @RequestParam(defaultValue = "descending") String mode) {
        return ResponseEntity.ok(bookService.getAllBooks(key, mode));
    }

    @PutMapping("/api/edit-book/{id}")
    public ResponseEntity<Void> editBook(@RequestBody BookRequest request, @PathVariable Long id) {
        bookService.editBookInfo(request, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(BookNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleBookNotFoundException(BookNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(BookUpdateException.class)
    public final ResponseEntity<ErrorResponse> handleBookUpdateException(BookUpdateException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @ExceptionHandler(BookDeletionException.class)
    public final ResponseEntity<ErrorResponse> handleBookDeletionException(BookDeletionException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }
}
