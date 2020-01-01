package dev.abekoh.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TodoHandler {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoHandler(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Mono<ServerResponse> get(ServerRequest request) {
        int taskId = Integer.parseInt(request.pathVariable("taskId"));
        return todoRepository.getOne(taskId)
                .flatMap(b -> ServerResponse
                        .created(request.uriBuilder().build())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(b)));
    }
}
