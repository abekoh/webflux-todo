package dev.abekoh.todo.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.abekoh.todo.config.AppConfiguration;
import dev.abekoh.todo.config.RouteConfiguration;
import dev.abekoh.todo.entity.Task;
import dev.abekoh.todo.repository.TaskRepository;
import dev.abekoh.todo.repository.TaskRepositoryImpl;
import dev.abekoh.todo.service.TaskServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringJUnitConfig
@WebFluxTest(controllers = TaskHandler.class)
@ActiveProfiles("test")
@Import({TaskServiceImpl.class, TaskRepositoryImpl.class, AppConfiguration.class, RouteConfiguration.class})
class TaskHandlerMTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    TaskRepository repository;

    @Autowired
    private WebTestClient webClient;

    @AfterEach
    void cleanUp() {
        Mockito.clearInvocations(repository);
    }

    @Nested
    class addOne {
        @Test
        @DisplayName("1件追加")
        void addOneSuccess() throws JsonProcessingException {
            Task input = new Task().toBuilder()
                    .text("やること")
                    .taskListId(1L)
                    .priorityRank(0L)
                    .build();

            Task expected = new Task().toBuilder()
                    .createdOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                    .updatedOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                    .text("やること")
                    .deadline(LocalDateTime.of(2020, 1, 1, 0, 0, 1))
                    .completed(false)
                    .deleted(false)
                    .priorityRank(0L)
                    .taskListId(1L)
                    .build();

            Mockito.when(repository.add(any()))
                    .thenReturn(Mono.just(expected));

            webClient.post()
                    .uri("/api/v1/todo/tasks")
                    .body(BodyInserters.fromValue(input))
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Task.class)
                    .isEqualTo(expected);

            Mockito.verify(repository, Mockito.times(1)).add(any());
        }
    }

    @Nested
    class getOne {
        @Test
        @DisplayName("1件取得")
        void getOneSuccess() {
            Task expected = new Task().toBuilder()
                    .taskId(1L)
                    .text("やること")
                    .build();

            Mockito.when(repository.getById(1L))
                    .thenReturn(Mono.just(expected));

            webClient.get()
                    .uri("/api/v1/todo/tasks/1")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Task.class)
                    .isEqualTo(expected);

            Mockito.verify(repository, Mockito.times(1)).getById(1L);
        }
    }

    @Nested
    class getAll {
        @Test
        @DisplayName("全件取得")
        void getAllSuccess() {
            List<Task> expected = List.of(
                    new Task().toBuilder()
                            .taskId(1L)
                            .text("やること1")
                            .build(),
                    new Task().toBuilder()
                            .taskId(2L)
                            .text("やること2")
                            .build()
            );

            Mockito.when(repository.getAll())
                    .thenReturn(Flux.fromIterable(expected));

            webClient.get()
                    .uri("/api/v1/todo/tasks")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$[0]").isEqualTo(expected.get(0))
                    .jsonPath("$[1]").isEqualTo(expected.get(1));

            Mockito.verify(repository, Mockito.times(1)).getAll();
        }
    }

    @Nested
    class updateOne {
        @Test
        @DisplayName("1件更新")
        void updateOneSuccess() {
            Task input = new Task().toBuilder()
                    .taskId(1L)
                    .createdOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                    .updatedOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                    .text("更新した")
                    .deadline(LocalDateTime.of(2020, 1, 1, 0, 0, 1))
                    .completed(false)
                    .deleted(false)
                    .priorityRank(0L)
                    .taskListId(1L)
                    .build();

            Task expectedTarget = new Task().toBuilder()
                    .taskId(1L)
                    .createdOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                    .updatedOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                    .text("更新前")
                    .deadline(LocalDateTime.of(2020, 1, 1, 0, 0, 1))
                    .completed(false)
                    .deleted(false)
                    .priorityRank(0L)
                    .taskListId(1L)
                    .build();

            Mockito.when(repository.getById(1L))
                    .thenReturn(Mono.just(expectedTarget));
            Mockito.when(repository.update(any()))
                    .thenReturn(Mono.just(1));

            webClient.patch()
                    .uri("/api/v1/todo/tasks/1")
                    .body(BodyInserters.fromValue(input))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Integer.class)
                    .isEqualTo(1);

            Mockito.verify(repository, Mockito.times(1)).getById(1L);
            Mockito.verify(repository, Mockito.times(1)).update(any());
        }
    }

    @Nested
    class removeOne {
        @Test
        @DisplayName("1件削除")
        void removeOneSuccess() {
            Task expectedTarget = new Task().toBuilder()
                    .taskId(1L)
                    .createdOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                    .updatedOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                    .text("更新前")
                    .deadline(LocalDateTime.of(2020, 1, 1, 0, 0, 1))
                    .completed(false)
                    .deleted(false)
                    .priorityRank(0L)
                    .taskListId(1L)
                    .build();

            Mockito.when(repository.removeById(1L))
                    .thenReturn(Mono.just(1));

            webClient.delete()
                    .uri("/api/v1/todo/tasks/1")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Integer.class)
                    .isEqualTo(1);

            Mockito.verify(repository, Mockito.times(1)).removeById(1L);
        }
    }
}