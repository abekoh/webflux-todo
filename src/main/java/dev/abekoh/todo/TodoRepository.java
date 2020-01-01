package dev.abekoh.todo;

import dev.abekoh.todo.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class TodoRepository {

    private final DatabaseClient databaseClient;

    @Autowired
    public TodoRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public Mono<Task> getOne(int taskId) {
        return databaseClient
                .select().from("task")
                .matching(Criteria.where("task_id").is(taskId))
                .as(Task.class).fetch().one()
                .switchIfEmpty(Mono.error(new RuntimeException("Task is not found.")));
    }
}
