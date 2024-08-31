package ru.otus.hw.handler;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PageHandlerImpl implements PageHandler {
    @Override
    public Mono<ServerResponse> index(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_HTML)
                .bodyValue(new ClassPathResource("templates/index.html"));
    }
}
