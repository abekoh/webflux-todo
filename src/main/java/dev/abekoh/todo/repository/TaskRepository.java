package dev.abekoh.todo.repository;

import dev.abekoh.todo.entity.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskRepository {

    Mono<Void> add(Mono<Task> task);

    Mono<Task> getById(int taskId);

    Flux<Task> getAll();

    Mono<Task> update(Mono<Task> task);

    boolean removeById(int taskId);

}
