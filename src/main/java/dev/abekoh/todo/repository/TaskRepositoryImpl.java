package dev.abekoh.todo.repository;

import dev.abekoh.todo.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final DatabaseClient databaseClient;

    @Autowired
    public TaskRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Void> add(Mono<Task> task) {
        return task.flatMap(t -> databaseClient
                .insert()
                .into(Task.class)
                .using(t)
                .then());
    }

    @Override
    public Mono<Task> getById(int taskId) {
        return databaseClient
                .select().from("task")
                .matching(Criteria.where("task_id").is(taskId))
                .as(Task.class).fetch().one()
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<Task> getAll() {
        return null;
    }

    @Override
    public Mono<Task> update(Mono<Task> task) {
        return null;
    }

    @Override
    public boolean removeById(int taskId) {
        return false;
    }
}
