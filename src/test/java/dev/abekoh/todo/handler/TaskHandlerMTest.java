package dev.abekoh.todo.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.abekoh.todo.config.AppConfiguration;
import dev.abekoh.todo.config.RouteConfiguration;
import dev.abekoh.todo.entity.Task;
import dev.abekoh.todo.repository.TaskRepository;
import dev.abekoh.todo.repository.TaskRepositoryImpl;
import dev.abekoh.todo.service.TaskServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import reactor.test.StepVerifier;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringJUnitConfig
@WebFluxTest(controllers = TaskHandler.class)
@ActiveProfiles("test")
@Import({TaskServiceImpl.class, TaskRepositoryImpl.class, AppConfiguration.class, RouteConfiguration.class})
class TaskHandlerMTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    @Captor
    ArgumentCaptor<Mono<Task>> captor;
    @MockBean
    private TaskRepository repository;
    @MockBean
    private Clock clock;
    @Autowired
    private WebTestClient webClient;

    @BeforeEach
    void setup() {
        // 時間固定
        Clock fixedClock = Clock.fixed(
                LocalDate.of(2020, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault());
        Mockito.when(clock.instant())
                .thenReturn(fixedClock.instant());
        Mockito.when(clock.getZone())
                .thenReturn(fixedClock.getZone());
    }

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
        @DisplayName("1件更新、順序変更なし")
        void updateOneSuccess() {
            Task input = new Task().toBuilder()
                    .taskId(1L)
                    .text("更新した")
                    .deadline(LocalDateTime.of(2020, 1, 1, 0, 0, 1))
                    .completed(false)
                    .deleted(false)
                    .priorityRank(0L)
                    .taskListId(1L)
                    .build();

            Task oldOne = new Task().toBuilder()
                    .taskId(1L)
                    .createdOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                    .updatedOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                    .text("更新まえ")
                    .deadline(LocalDateTime.of(2020, 1, 1, 0, 0, 1))
                    .completed(false)
                    .deleted(false)
                    .priorityRank(0L)
                    .taskListId(1L)
                    .build();

            List<Task> oldAll = List.of(
                    oldOne
            );

            Task newOne = new Task().toBuilder()
                    .taskId(1L)
                    .createdOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                    .updatedOn(LocalDateTime.of(2020, 2, 1, 0, 0, 0))
                    .text("更新した")
                    .deadline(LocalDateTime.of(2020, 1, 1, 0, 0, 1))
                    .completed(false)
                    .deleted(false)
                    .priorityRank(0L)
                    .taskListId(1L)
                    .build();

            Mockito.when(repository.getAll())
                    .thenReturn(Flux.fromIterable(oldAll));
            Mockito.when(repository.getById(1L))
                    .thenReturn(Mono.just(oldOne));
            Mockito.when(repository.update(any()))
                    .thenReturn(Mono.just(1));

            webClient.patch()
                    .uri("/api/v1/todo/tasks/1")
                    .body(BodyInserters.fromValue(input))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Integer.class)
                    .isEqualTo(1);

            Mockito.verify(repository, Mockito.times(1)).getAll();
            Mockito.verify(repository, Mockito.times(1)).getById(1L);
            Mockito.verify(repository, Mockito.times(1)).update(captor.capture());

            StepVerifier.create(captor.getValue())
                    .expectNext(newOne)
                    .verifyComplete();
        }

        @ParameterizedTest(name = "taskId={0}をpriorityRank={1}にしたとき、taskId順にpriorityRankは{2},{3},{4}になる")
        @CsvSource({
                "1, 3, 3, 1, 2",
                "3, 1, 2, 3, 1"
        })
        @DisplayName("1件更新、順序変更あり")
        void updateOrderedSuccess(long from, long to, long order1, long order2, long order3) {
            Task input = new Task().toBuilder()
                    .taskId(from)
                    .priorityRank(to)
                    .build();

            List<Task> oldAll = List.of(
                    new Task().toBuilder()
                            .taskId(1L)
                            .priorityRank(1L)
                            .build(),
                    new Task().toBuilder()
                            .taskId(2L)
                            .priorityRank(2L)
                            .build(),
                    new Task().toBuilder()
                            .taskId(3L)
                            .priorityRank(3L)
                            .build()
            );

            List<Task> newAll = List.of(
                    new Task().toBuilder()
                            .taskId(1L)
                            .priorityRank(order1)
                            .updatedOn(LocalDateTime.of(2020, 2, 1, 0, 0, 0))
                            .build(),
                    new Task().toBuilder()
                            .taskId(2L)
                            .priorityRank(order2)
                            .updatedOn(LocalDateTime.of(2020, 2, 1, 0, 0, 0))
                            .build(),
                    new Task().toBuilder()
                            .taskId(3L)
                            .priorityRank(order3)
                            .updatedOn(LocalDateTime.of(2020, 2, 1, 0, 0, 0))
                            .build()
            );

            Mockito.when(repository.getAll())
                    .thenReturn(Flux.fromIterable(oldAll));

            Mockito.when(repository.getById(from))
                    .thenReturn(Mono.just(oldAll.get((int) (from - 1L))));

            Mockito.when(repository.update(any()))
                    .thenReturn(Mono.just(1));

            webClient.patch()
                    .uri("/api/v1/todo/tasks/" + from)
                    .body(BodyInserters.fromValue(input))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Integer.class)
                    .isEqualTo(3);

            Mockito.verify(repository, Mockito.times(1)).getAll();

            Mockito.verify(repository, Mockito.times(3)).getById(from);

            Mockito.verify(repository, Mockito.times(3)).update(captor.capture());
            for (int i = 0; i < captor.getAllValues().size(); i++) {
                StepVerifier.create(captor.getAllValues().get(i))
                        .expectNext(newAll.get(i))
                        .verifyComplete();
            }
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