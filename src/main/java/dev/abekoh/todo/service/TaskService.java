package dev.abekoh.todo.service;

import dev.abekoh.todo.entity.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

    Mono<Task> addTask(Mono<Task> task);

    Mono<Task> getTaskById(long taskId);

    Flux<Task> getTaskAll();

    Mono<Integer> updateTask(long taskId, Mono<Task> sourceTask);

    Mono<Integer> removeTaskById(long taskId);

    Mono<Integer> getNextId();
}
