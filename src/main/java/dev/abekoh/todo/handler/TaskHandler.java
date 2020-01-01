package dev.abekoh.todo.handler;

import dev.abekoh.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TaskHandler {

    private final TaskService service;

    @Autowired
    public TaskHandler(TaskService service) {
        this.service = service;
    }

    public Mono<ServerResponse> addOne(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> getOne(ServerRequest request) {
        return this.service.getTaskById(Integer.parseInt(request.pathVariable("taskId")))
                .flatMap(b -> ServerResponse
                        .created(request.uriBuilder().build())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(b)));
    }

    public Flux<ServerResponse> getAll(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> updateOne(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> removeOne(ServerRequest request) {
        return null;
    }
}
