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
        return repository.getAll()
                .flatMap(t -> Mono.zip(Mono.just(t), repository.getById(taskId), sourceTask)
                        .flatMap(zipped -> {
                            // 今見ているタスク
                            Task cursor = zipped.getT1();
                            // 更新対象の前の状態
                            Task targetOld = zipped.getT2();
                            // 更新対象の新しい状態
                            Task targetNew = zipped.getT3();
                            // 変更対象ならそのまま更新
                            if (cursor.getTaskId() == taskId) {
                                // CREATED_ONはそのまま、UPDATED_ONは更新する
                                targetNew.setCreatedOn(targetOld.getCreatedOn());
                                targetNew.setUpdatedOn(LocalDateTime.now(clock));
                                return Mono.just(targetNew);
                            } else if (targetNew.getPriorityRank() <= cursor.getPriorityRank() && cursor.getPriorityRank() < targetOld.getPriorityRank()) {
                                cursor.incPriorityRank();
                                return Mono.just(cursor);
                            } else if (targetNew.getPriorityRank() < targetOld.getPriorityRank() && cursor.getPriorityRank().equals(targetOld.getPriorityRank())) {
                                cursor.setPriorityRank(targetNew.getPriorityRank());
                                return Mono.just(cursor);
                            } else if (targetOld.getPriorityRank() < cursor.getPriorityRank() && cursor.getPriorityRank() <= targetNew.getPriorityRank()) {
                                cursor.decPriorityRank();
                                return Mono.just(cursor);
                            } else if (targetOld.getPriorityRank() < targetNew.getPriorityRank() && cursor.getPriorityRank() == targetNew.getPriorityRank()) {
                                cursor.setPriorityRank(targetOld.getPriorityRank());
                                return Mono.just(cursor);
                            }
                            return Mono.empty();
                        })
                        .flatMap(t2 -> repository.update(Mono.just(t2))))
                .reduce(Integer::sum);
    }

    @Override
    public Mono<Integer> removeTaskById(long taskId) {
        return repository.removeById(taskId);
    }
}
