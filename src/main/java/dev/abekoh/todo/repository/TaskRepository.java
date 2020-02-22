package dev.abekoh.todo.repository;

import dev.abekoh.todo.entity.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskRepository {

    Mono<Task> add(Mono<Task> task);

    Mono<Task> getById(long taskId);

    Flux<Task> getAll();

    Mono<Integer> update(Mono<Task> task);

    Mono<Integer> removeById(long taskId);

    Mono<Integer> getNextId();
}
