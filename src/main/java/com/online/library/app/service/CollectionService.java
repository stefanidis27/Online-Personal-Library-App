package com.online.library.app.service;

import com.online.library.app.dto.request.CollectionRequest;
import com.online.library.app.dto.response.BookResponse;
import com.online.library.app.dto.response.CollectionResponse;
import com.online.library.app.exceptionhandler.collection.CollectionCreationException;
import com.online.library.app.exceptionhandler.collection.CollectionDeletionException;
import com.online.library.app.exceptionhandler.collection.CollectionUpdateException;
import com.online.library.app.exceptionhandler.collection.CollectionNotFoundException;
import com.online.library.app.exceptionhandler.user.UserNotFoundException;
import com.online.library.app.model.User;
import com.online.library.app.model.collection.Collection;
import com.online.library.app.model.collection.CollectionDetails;
import com.online.library.app.repository.CollectionDetailsRepository;
import com.online.library.app.repository.CollectionRepository;
import com.online.library.app.repository.UserRepository;
import com.online.library.app.service.interfaces.CollectionServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService implements CollectionServiceInterface {

    private final UserRepository userRepository;
    private final CollectionRepository collectionRepository;
    private final CollectionDetailsRepository collectionDetailsRepository;

    @Override
    public void createCollection(CollectionRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with this id was not found."));

        Collection collection = new Collection();
        collection.setName(request.getName());
        collection.setBooks(new ArrayList<>());
        CollectionDetails collectionDetails = new CollectionDetails();
        collectionDetails.setCollection(collection);
        collectionDetails.setCategory(request.getCategory());
        collectionDetails.setNumberOfBooks(0);
        collection.setCollectionDetails(collectionDetails);

        user.getCollections().add(collection);
        user.setNumberOfCreatedCollections(user.getNumberOfCreatedCollections() + 1);
        collection.setUser(user);

        try {
            collectionDetailsRepository.save(collectionDetails);
            collectionRepository.save(collection);
            userRepository.save(user);
        } catch (DataAccessException e) {
            throw new CollectionCreationException("Collection creation failed.");
        }
    }

    @Override
    public void deleteCollection(Long id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new CollectionNotFoundException("Collection with this id not found."));

        collection.getUser().setNumberOfCreatedCollections(
                collection.getUser().getNumberOfCreatedCollections() - 1);
        collection.getUser().getCollections().remove(collection);
        collection.getBooks().forEach(book -> book.getCollections().remove(collection));
        try {
            collectionRepository.delete(collection);
        } catch (DataAccessException e) {
            throw new CollectionDeletionException("Collection with this id could not be deleted.");
        }
    }

    @Override
    public List<CollectionResponse> getAllCollections() {
        return collectionRepository.findAll().stream().map(collection -> new CollectionResponse(
                collection.getId(),
                collection.getName(),
                collection.getCollectionDetails().getCategory(),
                collection.getCollectionDetails().getNumberOfBooks(),
                collection.getBooks().stream().map(book -> new BookResponse(
                        book.getId(),
                        book.getName(),
                        book.getBookDetails().getCategory(),
                        book.getBookDetails().getAuthor(),
                        book.getBookDetails().getPublishingHouse(),
                        book.getBookDetails().getPublicationDate())).toList()
        )).toList();
    }

    @Override
    public List<CollectionResponse> getAllCollectionsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with this id was not found."));

        return collectionRepository.findAllByUser(user).stream().map(collection -> new CollectionResponse(
                collection.getId(),
                collection.getName(),
                collection.getCollectionDetails().getCategory(),
                collection.getCollectionDetails().getNumberOfBooks(),
                collection.getBooks().stream().map(book -> new BookResponse(
                        book.getId(),
                        book.getName(),
                        book.getBookDetails().getCategory(),
                        book.getBookDetails().getAuthor(),
                        book.getBookDetails().getPublishingHouse(),
                        book.getBookDetails().getPublicationDate())).toList()
        )).toList();
    }

    @Override
    public CollectionResponse getCollectionById(Long id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new CollectionNotFoundException("Collection with this id not found."));

        return new CollectionResponse(
                collection.getId(),
                collection.getName(),
                collection.getCollectionDetails().getCategory(),
                collection.getCollectionDetails().getNumberOfBooks(),
                collection.getBooks().stream().map(book -> new BookResponse(
                        book.getId(),
                        book.getName(),
                        book.getBookDetails().getCategory(),
                        book.getBookDetails().getAuthor(),
                        book.getBookDetails().getPublishingHouse(),
                        book.getBookDetails().getPublicationDate())).toList());
    }

    @Override
    public void editCollectionInfo(CollectionRequest request, Long id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new CollectionNotFoundException("Collection with this id not found."));

        if (request.getName() != null) {
            collection.setName(request.getName());
        }
        if (request.getCategory() != null) {
            collection.getCollectionDetails().setCategory(request.getCategory());
        }

        try {
            collectionRepository.save(collection);
        } catch (DataAccessException e) {
            throw new CollectionUpdateException("Collection with this id could not be updated.");
        }
    }
}
