package dev.abekoh.todo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskList {

    private Long taskListId;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private String name;

    private Long priorityRank;

}
