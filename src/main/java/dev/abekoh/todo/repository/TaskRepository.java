package dev.abekoh.todo.repository;

import dev.abekoh.todo.entity.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskRepository {

    Mono<Task> add(Task task);

    Mono<Task> getById(int taskId);

    Flux<Task> getAll();

    Mono<Task> update(Task task);

    boolean removeById(int taskId);

}
