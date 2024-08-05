package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"book"})
@EqualsAndHashCode(exclude = {"book"})
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    @Field("text")
    private String text;

    @Field("book")
    private Book book;
}
