package dev.abekoh.todo.handler;

import dev.abekoh.todo.config.RouteConfiguration;
import dev.abekoh.todo.entity.Task;
import dev.abekoh.todo.repository.TaskRepository;
import dev.abekoh.todo.service.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = TaskHandler.class)
@Import({TaskServiceImpl.class, RouteConfiguration.class})
class TaskHandler_LTest {

    @MockBean
    TaskRepository repository;

    @Autowired
    private WebTestClient webClient;


    @Test
    void testGetOne() {
        Task task = new Task();
        task.setTaskId(1L);
        task.setText("やること");

        Mockito.when(repository.getById(1L))
                .thenReturn(Mono.just(task));

        webClient.get()
                .uri("/api/v1/todo/tasks/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.taskId").isEqualTo(1L)
                .jsonPath("$.text").isEqualTo("やること");


        Mockito.verify(repository, Mockito.times(1)).getById(1L);
    }
}