package dev.abekoh.todo.handler;

import dev.abekoh.todo.entity.Task;
import dev.abekoh.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TaskHandler {

    private final TaskService service;

    @Autowired
    public TaskHandler(TaskService service) {
        this.service = service;
    }

    public Mono<ServerResponse> addOne(ServerRequest request) {
        Mono<Task> requestTask = request.bodyToMono(Task.class);
        return ServerResponse
                .ok()
                .build(service.addTask(requestTask));
    }

    public Mono<ServerResponse> getOne(ServerRequest request) {
        int taskId = Integer.parseInt(request.pathVariable("taskId"));
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
