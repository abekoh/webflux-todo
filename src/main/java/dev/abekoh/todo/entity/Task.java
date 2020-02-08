package dev.abekoh.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table("task")
public class Task {

    @Id
    private Long taskId;

    private Long taskListId;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private String text;

    private LocalDateTime deadline;

    private Boolean completed;

    private Boolean deleted;

    private Long priorityRank;

    public void incPriorityRank() {
        this.priorityRank++;
    }

    public void decPriorityRank() {
        this.priorityRank--;
    }
}
