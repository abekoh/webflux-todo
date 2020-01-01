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
    public Mono<Task> add(Mono<Task> task) {
        return task.flatMap(t -> databaseClient
                .insert()
                .into(Task.class)
                .using(t)
                .fetch()
                .one()
                .map(map -> t
                        .toBuilder()
                        .taskId((long) map.get("LAST_INSERT_ID"))
                        .build())
        );
    }

    @Override
    public Mono<Task> getById(long taskId) {
        return databaseClient
                .select()
                .from("task")
                .matching(Criteria.where("task_id").is(taskId))
                .matching(Criteria.where("deleted").is("0"))
                .as(Task.class)
                .fetch()
                .one()
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<Task> getAll() {
        return databaseClient
                .select().from("task")
                .matching(Criteria.where("deleted").is("0"))
                .as(Task.class)
                .fetch()
                .all()
//                .take(50)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<Integer> update(Mono<Task> task) {
        return task.flatMap(t -> databaseClient
                .update()
                .table(Task.class)
                .using(t)
                .fetch()
                .rowsUpdated()
        );
    }

    @Override
    public Mono<Integer> removeById(long taskId) {
        return databaseClient
                .delete()
                .from(Task.class)
                .matching(Criteria.where("task_id").is(taskId))
                .fetch()
                .rowsUpdated();
    }
}
