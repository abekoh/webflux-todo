package dev.abekoh.todo.service;

import dev.abekoh.todo.entity.Task;
import dev.abekoh.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    @Autowired
    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Task> addTask(Mono<Task> task) {
        return repository.add(
                task.doOnNext(t -> {
                    t.setCreatedOn(LocalDateTime.now());
                    t.setUpdatedOn(LocalDateTime.now());
                }));
    }

    @Override
    public Mono<Task> getTaskById(long taskId) {
        return repository.getById(taskId);
    }

    @Override
    public Flux<Task> getTaskAll() {
        return null;
    }

    @Override
    public Mono<Integer> updateTask(Mono<Task> task) {
        return null;
    }

    @Override
    public Mono<Integer> removeTaskById(long taskId) {
        return null;
    }
}
