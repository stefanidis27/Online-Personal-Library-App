package com.online.library.app.model.book;

import com.online.library.app.model.collection.Collection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "book", orphanRemoval = true)
    private BookDetails bookDetails;
    @ManyToMany(mappedBy = "books")
    private List<Collection> collections;
}
