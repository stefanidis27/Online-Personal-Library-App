package com.online.library.app.model;

import com.online.library.app.model.collection.Collection;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String type;
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(name = "no_collections", nullable = false)
    private Integer numberOfCreatedCollections;
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Collection> collections;
}
