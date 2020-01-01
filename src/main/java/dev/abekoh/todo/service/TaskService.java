package dev.abekoh.todo.service;

import dev.abekoh.todo.entity.Task;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

    Mono<Task> addTask(Mono<Task> task);

    Mono<Task> getTaskById(long taskId);

    Flux<Task> getTaskAll();

    Mono<Integer> updateTask(Mono<Task> task);

    Mono<Integer> removeTaskById(long taskId);
}
