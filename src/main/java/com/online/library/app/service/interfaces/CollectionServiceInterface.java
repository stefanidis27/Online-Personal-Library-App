package com.online.library.app.service.interfaces;

import com.online.library.app.dto.request.CollectionRequest;
import com.online.library.app.dto.response.CollectionResponse;

import java.util.List;

public interface CollectionServiceInterface {
    void createCollection(CollectionRequest request, Long userId);

    void deleteCollection(Long id);

    List<CollectionResponse> getAllCollections();

    List<CollectionResponse> getAllCollectionsByUser(Long userId);

    CollectionResponse getCollectionById(Long id);

    void editCollectionInfo(CollectionRequest request, Long id);
}
