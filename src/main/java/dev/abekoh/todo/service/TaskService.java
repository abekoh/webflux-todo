package dev.abekoh.todo.service;

import dev.abekoh.todo.entity.Task;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

    Mono<Task> addTask(Task task);

    Mono<Task> getTaskById(int taskId);

    Flux<Task> getTaskAll();

    Mono<Task> updateTask(Task task);

    boolean removeTaskById(int taskId);
}
