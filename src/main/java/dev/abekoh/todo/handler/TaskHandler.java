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
        return service.addTask(request.bodyToMono(Task.class))
                .flatMap(task -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(task)))
                .switchIfEmpty(ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    public Mono<ServerResponse> getOne(ServerRequest request) {
        logger.info("getOne: " + request);
        long taskId = Long.parseLong(request.pathVariable("taskId"));
        return service.getTaskById(taskId)
                .flatMap(task -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(task)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        logger.info("getAll: " + request);
        return service.getTaskAll()
                .collectList()
                .flatMap(tasks -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(tasks)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> updateOne(ServerRequest request) {
        logger.info("updateOne: " + request);
        long taskId = Long.parseLong(request.pathVariable("taskId"));
        return service.updateTask(taskId, request.bodyToMono(Task.class))
                .flatMap(updatedCount -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(updatedCount)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> removeOne(ServerRequest request) {
        logger.info("removeOne: " + request);
        long taskId = Long.parseLong(request.pathVariable("taskId"));
        return service.removeTaskById(taskId)
                .flatMap(removedCount -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(removedCount)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
