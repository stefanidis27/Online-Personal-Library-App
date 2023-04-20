package com.online.library.app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    private String name;
    private String category;
    private String author;
    private String publishingHouse;
    private Timestamp publicationDate;
}
