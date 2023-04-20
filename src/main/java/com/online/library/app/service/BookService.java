package com.online.library.app.service;

import com.online.library.app.dto.request.BookRequest;
import com.online.library.app.dto.response.BookResponse;
import com.online.library.app.exceptionhandler.book.BookCreationException;
import com.online.library.app.exceptionhandler.book.BookDeletionException;
import com.online.library.app.exceptionhandler.book.BookNotFoundException;
import com.online.library.app.exceptionhandler.book.BookUpdateException;
import com.online.library.app.exceptionhandler.collection.CollectionNotFoundException;
import com.online.library.app.exceptionhandler.collection.CollectionUpdateException;
import com.online.library.app.model.book.Book;
import com.online.library.app.model.book.BookDetails;
import com.online.library.app.model.collection.Collection;
import com.online.library.app.repository.BookDetailsRepository;
import com.online.library.app.repository.BookRepository;
import com.online.library.app.repository.CollectionRepository;
import com.online.library.app.service.interfaces.BookServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService implements BookServiceInterface {

    private final BookRepository bookRepository;
    private final CollectionRepository collectionRepository;
    private final BookDetailsRepository bookDetailsRepository;

    @Override
    public void createBook(BookRequest request) {
        Book book = new Book();
        book.setCollections(new ArrayList<>());
        book.setName(request.getName());
        BookDetails bookDetails = new BookDetails();
        bookDetails.setBook(book);
        bookDetails.setCategory(request.getCategory());
        bookDetails.setAuthor(request.getAuthor());
        bookDetails.setPublishingHouse(request.getPublishingHouse());
        bookDetails.setPublicationDate(request.getPublicationDate());
        book.setBookDetails(bookDetails);

        try {
            bookDetailsRepository.save(bookDetails);
            bookRepository.save(book);
        } catch (DataAccessException e) {
            throw new BookCreationException("Book could not be created.");
        }
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with this id does not exist."));
        book.getCollections().forEach(collection -> {
            collection.getBooks().remove(book);
            collection.getCollectionDetails()
                    .setNumberOfBooks(collection.getCollectionDetails().getNumberOfBooks() - 1);
        });

        try {
            bookRepository.delete(book);
        } catch (DataAccessException e) {
            throw new BookDeletionException("Book could not be deleted.");
        }
    }

    @Override
    public List<BookResponse> getAllBooks(String sortKey, String mode) {
        Sort sort;
        if (mode.equals("ascending")) {
            sort = Sort.by(sortKey).ascending();
        } else {
            sort = Sort.by(sortKey).descending();
        }

        return bookDetailsRepository.findAll(sort).stream().map(bookDetails -> new BookResponse(
                bookDetails.getId(),
                bookDetails.getBook().getName(),
                bookDetails.getCategory(),
                bookDetails.getAuthor(),
                bookDetails.getPublishingHouse(),
                bookDetails.getPublicationDate()
        )).toList();
    }

    @Override
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with this id does not exist."));

        return new BookResponse(
                book.getId(),
                book.getName(),
                book.getBookDetails().getCategory(),
                book.getBookDetails().getAuthor(),
                book.getBookDetails().getPublishingHouse(),
                book.getBookDetails().getPublicationDate());
    }

    @Override
    public void editBookInfo(BookRequest request, Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with this id does not exist."));

        if (request.getName() != null) {
            book.setName(request.getName());
        }
        if (request.getCategory() != null) {
            book.getBookDetails().setCategory(request.getCategory());
        }
        if (request.getAuthor() != null) {
            book.getBookDetails().setAuthor(request.getAuthor());
        }
        if (request.getPublicationDate() != null) {
            book.getBookDetails().setPublicationDate(request.getPublicationDate());
        }
        if (request.getPublishingHouse() != null) {
            book.getBookDetails().setPublishingHouse(request.getPublishingHouse());
        }

        try {
            bookRepository.save(book);
        } catch (DataAccessException e) {
            throw new BookUpdateException("Book could not be updated.");
        }
    }

    @Override
    public void addBookToCollection(Long collectionId, Long bookId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException("Collection with this id not found."));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with this id does not exist."));

        collection.getBooks().add(book);
        collection.getCollectionDetails().setNumberOfBooks(collection.getCollectionDetails().getNumberOfBooks() + 1);
        book.getCollections().add(collection);

        try {
            bookRepository.save(book);
        } catch (DataAccessException e) {
            throw new BookUpdateException("Book could not be added to the collection.");
        }
        try {
            collectionRepository.save(collection);
        } catch (DataAccessException e) {
            throw new CollectionUpdateException("Collection could not be updated with new book.");
        }
    }

    @Override
    public void removeBookFromCollection(Long collectionId, Long bookId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException("Collection with this id not found."));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with this id does not exist."));

        collection.getBooks().remove(book);
        collection.getCollectionDetails().setNumberOfBooks(collection.getCollectionDetails().getNumberOfBooks() - 1);
        book.getCollections().remove(collection);

        try {
            bookRepository.save(book);
        } catch (DataAccessException e) {
            throw new BookUpdateException("Book could not be removed from the collection.");
        }
        try {
            collectionRepository.save(collection);
        } catch (DataAccessException e) {
            throw new CollectionUpdateException("Collection could not be updated with fewer books.");
        }
    }
}
