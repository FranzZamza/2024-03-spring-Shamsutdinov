package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = {"author", "genre"})
@EqualsAndHashCode(exclude = {"author", "genre"})
@Document(collection = "books")
public class Book {

    @Id
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    @Field("title")
    private String title;

    @Field("author")
    private Author author;

    @Field("genre")
    private Genre genre;

}