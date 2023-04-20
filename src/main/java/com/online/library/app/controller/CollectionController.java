package com.online.library.app.controller;

import com.online.library.app.dto.request.CollectionRequest;
import com.online.library.app.dto.response.CollectionResponse;
import com.online.library.app.exceptionhandler.ErrorResponse;
import com.online.library.app.exceptionhandler.collection.CollectionCreationException;
import com.online.library.app.exceptionhandler.collection.CollectionDeletionException;
import com.online.library.app.exceptionhandler.collection.CollectionUpdateException;
import com.online.library.app.service.interfaces.CollectionServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionServiceInterface collectionService;

    @PostMapping("/api/create-collection/for-user/{userId}")
    public ResponseEntity<CollectionResponse> createCollection(
            @RequestBody CollectionRequest request,
            @PathVariable Long userId) {
        collectionService.createCollection(request, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/api/delete-collection/{id}")
    public ResponseEntity<Void> deleteCollection(@PathVariable Long id) {
        collectionService.deleteCollection(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/collection/{id}")
    public ResponseEntity<CollectionResponse> getCollectionById(@PathVariable Long id) {
        return ResponseEntity.ok(collectionService.getCollectionById(id));
    }

    @GetMapping("/api/collections")
    public ResponseEntity<List<CollectionResponse>> getCollections() {
        return ResponseEntity.ok(collectionService.getAllCollections());
    }

    @GetMapping("/api/collections-for-user/{userId}")
    public ResponseEntity<List<CollectionResponse>> getCollectionsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(collectionService.getAllCollectionsByUser(userId));
    }

    @PutMapping("/api/edit-collection/{id}")
    public ResponseEntity<Void> editCollection(@RequestBody CollectionRequest request, @PathVariable Long id) {
        collectionService.editCollectionInfo(request, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(CollectionUpdateException.class)
    public final ResponseEntity<ErrorResponse> handleCollectionUpdateException(CollectionUpdateException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @ExceptionHandler(CollectionDeletionException.class)
    public final ResponseEntity<ErrorResponse> handleCollectionDeletionException(CollectionDeletionException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @ExceptionHandler(CollectionCreationException.class)
    public final ResponseEntity<ErrorResponse> handleCollectionCreationException(CollectionCreationException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }
}
