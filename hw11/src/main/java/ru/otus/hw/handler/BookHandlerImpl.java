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
        return bookRepository.findBookByIdWithCommentsLimit(id, 0)
                .doOnNext(System.out::println)
                .flatMap(book -> ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(bookMapper.toDto(book)))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id:%s not found".formatted(id))));
    }

    @Override
    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(BookDto.class)
                .flatMap(bookDto -> {
                    Book book = bookMapper.toBook(bookDto);
                    book.setId(UUID.randomUUID().toString());
                    Mono<Author> authorCheck = findAuthorByNameOrThrow(book);
                    Mono<Genre> genreCheck = findGenreByNameOrThrow(book);
                    return Mono.zip(authorCheck, genreCheck)
                            .then(bookRepository.save(book))
                            .then(ServerResponse.ok().build());
                }).onErrorResume(buildResponseWith404Status());
    }

    @Override
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable(ID_PATH_VARIABLE);
        return bookRepository.findById(id)
                .flatMap(bookRepository::delete)
                .then(ok().build());
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(BookDto.class)
                .flatMap(bookDto -> {
                    Book book = bookMapper.toBook(bookDto);
                    var bookMono = bookRepository.findBookByIdWithCommentsLimit(book.getId(),0);
                    var authorMono = findAuthorByNameOrThrow(book);
                    var genreMono = findGenreByNameOrThrow(book);
                    return updateBook(authorMono, genreMono, bookMono, book);
                })
                .then(ok().build())
                .onErrorResume(buildResponseWith404Status());
    }

    private Mono<Book> updateBook(Mono<Author> authorMono, Mono<Genre> genreMono, Mono<Book> bookMono, Book book) {
            return Mono.zip(authorMono, genreMono, bookMono)
                .flatMap(tuple -> {
                    var author = tuple.getT1();
                    var genre = tuple.getT2();
                    var bookForUpdate = tuple.getT3();

                    bookForUpdate.setAuthor(author);
                    bookForUpdate.setGenre(genre);
                    bookForUpdate.setTitle(book.getTitle());

                    return bookRepository.save(bookForUpdate);
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

}