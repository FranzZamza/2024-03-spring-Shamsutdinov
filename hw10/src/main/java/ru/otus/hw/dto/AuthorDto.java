package ru.otus.hw.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthorDto {
    private long id;

    private String fullName;
}
