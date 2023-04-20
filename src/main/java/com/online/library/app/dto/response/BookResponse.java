package com.online.library.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

    private Long id;
    private String name;
    private String category;
    private String author;
    private String publishingHouse;
    private Timestamp publicationDate;
}
