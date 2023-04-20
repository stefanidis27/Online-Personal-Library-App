package com.online.library.app.model.collection;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "collection_details")
public class CollectionDetails {

    @Id
    @Column(name = "collection_id")
    private Long id;
    private String category;
    @Column(name = "no_books", nullable = false)
    private Integer numberOfBooks;
    @MapsId
    @OneToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;
}
