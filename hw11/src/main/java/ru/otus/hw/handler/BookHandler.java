package ru.otus.hw.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface BookHandler {
    Mono<ServerResponse> findAll(ServerRequest serverRequest);

    Mono<ServerResponse> findById(ServerRequest serverRequest);

    Mono<ServerResponse> save(ServerRequest serverRequest);

    Mono<ServerResponse> delete(ServerRequest serverRequest);

    Mono<ServerResponse> update(ServerRequest serverRequest);
}
