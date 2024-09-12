package ru.otus.hw.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BookDto {
    private long id;

    private String title;

    private String authorName;

    private String genreName;
}
