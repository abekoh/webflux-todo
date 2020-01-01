package dev.abekoh.todo.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class Task {

    private long taskId;

    private long taskListId;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private String text;

    private LocalDateTime deadline;

    private Boolean completed;

    private Boolean deleted;

    private long priorityRank;
}
