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
        return repository.getAll();
    }

    @Override
    public Mono<Integer> updateTask(long taskId, Mono<Task> sourceTask) {
        return sourceTask
                // 変更予定のエンティティとtaskIdが不一致ならば殻にする
                .flatMap(t -> t.getTaskId() == taskId ? Mono.just(t) : Mono.empty())
                .zipWith(repository.getById(taskId))
                .doOnNext(t -> {
                    Task source = t.getT1();
                    Task existed = t.getT2();
                    // CREATED_ONはそのまま、UPDATED_ONは更新する
                    source.setCreatedOn(existed.getCreatedOn());
                    source.setUpdatedOn(LocalDateTime.now());
                })
                .flatMap(t -> repository.update(Mono.just(t.getT1())));
    }

    @Override
    public Mono<Integer> removeTaskById(long taskId) {
        return repository.removeById(taskId);
    }
}
