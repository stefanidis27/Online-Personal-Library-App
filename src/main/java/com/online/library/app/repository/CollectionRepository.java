package com.online.library.app.repository;

import com.online.library.app.model.User;
import com.online.library.app.model.collection.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findAllByUser(User user);
}
