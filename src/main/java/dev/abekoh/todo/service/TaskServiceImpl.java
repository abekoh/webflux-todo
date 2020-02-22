package dev.abekoh.todo.service;

import dev.abekoh.todo.entity.Task;
import dev.abekoh.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    private final Clock clock;

    @Autowired
    public TaskServiceImpl(TaskRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public Mono<Task> addTask(Mono<Task> task) {
        return repository.add(
                task.doOnNext(t -> {
                    t.setTaskId(null);
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
        return repository.getAll();
    }

    @Override
    public Mono<Integer> updateTask(long taskId, Mono<Task> sourceTask) {
        return sourceTask
                // 変更予定のエンティティとtaskIdが不一致ならば空にする
                .flatMap(t -> t.getTaskId() == taskId ? Mono.just(t) : Mono.empty())
                .zipWith(repository.getById(taskId), (source, existed) -> {
                    source.setCreatedOn(existed.getCreatedOn());
                    source.setUpdatedOn(LocalDateTime.now(clock));
                    return source;
                })
                .flatMap(t -> repository.update(Mono.just(t)));
    }

    @Override
    public Mono<Integer> removeTaskById(long taskId) {
        return repository.removeById(taskId);
    }

    @Override
    public Mono<Integer> getNextId() {
        return repository.getNextId();
    }
}
