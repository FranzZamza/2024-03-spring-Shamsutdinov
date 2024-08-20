package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.hw.handler.AuthorHandler;
import ru.otus.hw.handler.BookHandler;
import ru.otus.hw.handler.GenreHandler;
import ru.otus.hw.handler.PageHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterConfig {

    private static final String ALL_AUTHORS_REQUEST = "api/v1/authors";

    private static final String ALL_BOOKS_REQUEST = "api/v1/books";

    private static final String BOOK_BY_ID_REQUEST = "api/v1/books/{id}";

    private static final String CREATE_BOOK_REQUEST = "api/v1/books";

    private static final String UPDATE_BOOK_REQUEST = "api/v1/books/{id}";

    private static final String ALL_GENRES = "api/v1/genres";

    private final BookHandler bookHandler;

    private final AuthorHandler authorHandler;

    private final GenreHandler genreHandler;

    private final PageHandler pageHandler;


    @Bean
    public RouterFunction<ServerResponse> booksRoutes() {
        return route()
                .GET(ALL_BOOKS_REQUEST, accept(APPLICATION_JSON), bookHandler::findAll)
                .GET(BOOK_BY_ID_REQUEST, accept(APPLICATION_JSON), bookHandler::findById)
                .POST(CREATE_BOOK_REQUEST, accept(APPLICATION_JSON), bookHandler::save)
                .DELETE(BOOK_BY_ID_REQUEST, accept(APPLICATION_JSON), bookHandler::delete)
                .PUT(UPDATE_BOOK_REQUEST, accept(APPLICATION_JSON), bookHandler::update)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> genreRoutes() {
        return route().GET(ALL_GENRES, accept(APPLICATION_JSON), genreHandler::findAll)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> authorsRoutes() {
        return route()
                .GET(ALL_AUTHORS_REQUEST, accept(APPLICATION_JSON), authorHandler::findAll)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> pageRoutes() {
        return route(GET("/"), pageHandler::index);
    }

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions
                .nest(path("/api"),
                        RouterFunctions.route()
                                .add(booksRoutes())
                                .add(authorsRoutes())
                                .add(genreRoutes())
                                .add(pageRoutes())
                                .build()
                );
    }
}
