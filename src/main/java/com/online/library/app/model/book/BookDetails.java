package com.online.library.app.model.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_details")
public class BookDetails {

    @Id
    @Column(name = "book_id")
    private Long id;
    private String category;
    private String author;
    @Column(nullable = false)
    private String publishingHouse;
    @Column(name = "publication_date", nullable = false)
    private Timestamp publicationDate;
    @MapsId
    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
