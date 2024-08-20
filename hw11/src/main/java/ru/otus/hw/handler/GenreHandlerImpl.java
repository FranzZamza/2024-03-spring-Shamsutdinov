package ru.otus.hw.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.GenreRepository;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class GenreHandlerImpl implements GenreHandler {

    private final GenreRepository genreRepository;

    @Override
    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ok().body(genreRepository.findAll(), Genre.class);
    }
}
