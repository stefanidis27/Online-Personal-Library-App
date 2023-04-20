package com.online.library.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionResponse {

    private Long id;
    private String name;
    private String category;
    private Integer numberOfBooks;
    private List<BookResponse> books;
}
