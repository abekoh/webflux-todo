package dev.abekoh.todo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {

    private int taskId;

    private int taskListId;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private String text;

    private LocalDateTime deadline;

    private Boolean completed;

    private Boolean deleted;

    private int priorityRank;
}
