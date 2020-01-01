package dev.abekoh.todo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskList {

    private long taskListId;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private String name;

    private long priorityRank;

}
