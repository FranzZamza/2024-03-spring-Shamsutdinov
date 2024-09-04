package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.model.Book;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {

    @Mappings({
            @Mapping(source = "author.fullName", target = "authorName"),
            @Mapping(source = "genre.name", target = "genreName")
    })
    BookDto toDto(Book book);

    @Mappings({
            @Mapping(source = "authorName", target = "author.fullName"),
            @Mapping(source = "genreName", target = "genre.name")
    })
    Book toBook(BookDto bookDto);

    List<BookDto> toDto(List<Book> books);
}
