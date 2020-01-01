package dev.abekoh.todo.handler;

import dev.abekoh.todo.entity.Task;
import dev.abekoh.todo.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TaskHandler {

    private static final Logger logger = LoggerFactory.getLogger(TaskHandler.class);

    private final TaskService service;

    @Autowired
    public TaskHandler(TaskService service) {
        this.service = service;
    }

    public Mono<ServerResponse> addOne(ServerRequest request) {
        logger.info("addOne: " + request);
        Mono<Task> taskMono = service.addTask(request.bodyToMono(Task.class));
        return taskMono.flatMap(task -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(task)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    public Mono<ServerResponse> getOne(ServerRequest request) {
        logger.info("getOne: " + request);
        long taskId = Long.parseLong(request.pathVariable("taskId"));
        Mono<ServerResponse> notFound = ServerResponse
                .notFound()
                .build();
        Mono<Task> taskMono = service.getTaskById(taskId);
        return taskMono.flatMap(task -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(task)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> updateOne(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> removeOne(ServerRequest request) {
        return null;
    }
}
