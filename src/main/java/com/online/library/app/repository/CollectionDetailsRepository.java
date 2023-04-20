package com.online.library.app.repository;

import com.online.library.app.model.collection.CollectionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionDetailsRepository extends JpaRepository<CollectionDetails, Long> {
}
