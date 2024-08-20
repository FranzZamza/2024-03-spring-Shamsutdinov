package ru.otus.hw.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.Author;
import ru.otus.hw.repository.AuthorRepository;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class AuthorHandlerImpl implements AuthorHandler {

    private final AuthorRepository authorRepository;

    @Override
    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ok().body(authorRepository.findAll(), Author.class);
    }
}
