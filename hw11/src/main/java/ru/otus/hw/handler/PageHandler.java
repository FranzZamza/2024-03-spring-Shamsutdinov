package ru.otus.hw.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface PageHandler {
    Mono<ServerResponse> index(ServerRequest request);
}
