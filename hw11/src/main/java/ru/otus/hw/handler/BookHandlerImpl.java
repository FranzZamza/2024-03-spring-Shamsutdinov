package ru.otus.hw.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.AuthorRepository;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.GenreRepository;

import java.util.UUID;
import java.util.function.Function;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Log4j2
@Component
@RequiredArgsConstructor
public class BookHandlerImpl implements BookHandler {

    private static final String ID_PATH_VARIABLE = "id";

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    @Override
    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ok()
                .contentType(APPLICATION_JSON)
                .body(bookRepository.findAll().map(bookMapper::toDto), BookDto.class);
    }

    @Override
    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable(ID_PATH_VARIABLE);
        return bookRepository.findById(id)
                .flatMap(book -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(bookMapper.toDto(book)))
                .switchIfEmpty(creteMonoErrorBookNotFound(id))
                .onErrorResume(buildResponseWith404Status());
    }

    @Override
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(BookDto.class)
                .flatMap(bookDto -> {
                    Book book = bookMapper.toBook(bookDto);
                    book.setId(UUID.randomUUID().toString());
                    Mono<Author> authorMono = findAuthorByNameOrThrow(book);
                    Mono<Genre> genreMono = findGenreByNameOrThrow(book);
                    return Mono.zip(authorMono, genreMono)
                            .flatMap(tuple -> {
                                book.setAuthor(tuple.getT1());
                                book.setGenre(tuple.getT2());
                                return bookRepository.save(book);
                            })
                            .then(ServerResponse.ok().build());
                }).onErrorResume(buildResponseWith404Status());
    }

    @Override
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable(ID_PATH_VARIABLE);
        return bookRepository
                .existsById(id)
                .flatMap(result -> {
                    if (result) {
                        return bookRepository
                                .deleteById(id)
                                .then(ServerResponse.ok().build());
                    }
                    return creteMonoErrorBookNotFound(id);
                })
                .onErrorResume(buildResponseWith404Status());
    }



    @Override
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable(ID_PATH_VARIABLE);

        return serverRequest
                .bodyToMono(BookDto.class)
                .flatMap(bookDto -> {
                    Book book = bookMapper.toBook(bookDto);
                    book.setId(id);
                    var authorMono = findAuthorByNameOrThrow(book);
                    var genreMono = findGenreByNameOrThrow(book);
                    return updateBook(authorMono, genreMono, book);
                }).then(ok().build())
                .onErrorResume(buildResponseWith404Status());
    }

    private Mono<Book> updateBook(Mono<Author> authorMono, Mono<Genre> genreMono, Book book) {
        return Mono.zip(authorMono, genreMono)
                .flatMap(tuple -> {
                    var author = tuple.getT1();
                    var genre = tuple.getT2();
                    book.setAuthor(author);
                    book.setGenre(genre);
                    return bookRepository.save(book);
                });
    }

    private static Function<Throwable, Mono<? extends ServerResponse>> buildResponseWith404Status() {
        return ex -> ServerResponse.status(HttpStatus.NOT_FOUND)
                .bodyValue(ex.getMessage());
    }

    private Mono<Author> findAuthorByNameOrThrow(Book book) {
        String fullName = book.getAuthor().getFullName();
        return authorRepository.findByFullName(fullName)
                .switchIfEmpty(
                        Mono.error(
                                new EntityNotFoundException("Author with full name %s not found"
                                        .formatted(fullName))));
    }

    private Mono<Genre> findGenreByNameOrThrow(Book book) {
        String name = book.getGenre().getName();
        return genreRepository.findByName(name)
                .switchIfEmpty(
                        Mono.error(
                                new EntityNotFoundException("Genre with name %s not found".formatted(name))));
    }

    private Mono<ServerResponse> creteMonoErrorBookNotFound(String id) {
        return Mono.error(new EntityNotFoundException("Book with id:%s not found".formatted(id)));
    }
}