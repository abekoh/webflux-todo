package dev.abekoh.todo.repository;

import dev.abekoh.todo.entity.Task;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.FileCopyUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


@SpringJUnitConfig
@DataR2dbcTest
@ActiveProfiles("test")
class TaskRepositoryImplDBTest {

    private static final Task expected01 = new Task().toBuilder()
            .createdOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
            .updatedOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
            .taskId(1L)
            .text("ご飯食べる")
            .deadline(LocalDateTime.of(2020, 1, 1, 0, 0, 1))
            .completed(false)
            .deleted(false)
            .priorityRank(0L)
            .taskListId(1L)
            .build();

    private static final Task expected02 = new Task().toBuilder()
            .createdOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
            .updatedOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
            .taskId(2L)
            .text("寝る")
            .deadline(LocalDateTime.of(2020, 1, 1, 0, 0, 1))
            .completed(false)
            .deleted(false)
            .priorityRank(0L)
            .taskListId(1L)
            .build();

    private static final Task expected03 = new Task().toBuilder()
            .createdOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
            .updatedOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
            .taskId(3L)
            .text("完了した")
            .deadline(LocalDateTime.of(2020, 1, 1, 0, 0, 1))
            .completed(true)
            .deleted(false)
            .priorityRank(0L)
            .taskListId(1L)
            .build();

    @Autowired
    private DatabaseClient databaseClient;
    private TaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = new TaskRepositoryImpl(this.databaseClient);
    }

    @AfterEach
    void cleanUp() {
        // テスト後に毎回data.sqlを実行
        Resource resource = new ClassPathResource("data.sql");
        String initializeSql;
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            initializeSql = FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        databaseClient.execute(initializeSql).then().block();
    }

    @Nested
    class add {
        @Test
        @DisplayName("1件追加")
        void addOne() {
            // taskIdのみnull
            Task input = new Task().toBuilder()
                    .createdOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                    .updatedOn(LocalDateTime.of(2020, 1, 1, 0, 00, 0))
                    .text("追加")
                    .deadline(LocalDateTime.of(2020, 1, 1, 0, 0, 1))
                    .completed(false)
                    .deleted(false)
                    .priorityRank(0L)
                    .taskListId(1L)
                    .build();
            // 結果には付与される
            Task expected = input.toBuilder()
                    .taskId(5L)
                    .build();

            Mono<Task> actual = repository.add(Mono.just(input));
            StepVerifier.create(actual)
                    .expectNext(expected)
                    .verifyComplete();
        }
    }

    @Nested
    class getById {
        @Test
        @DisplayName("1件取得")
        void getOne() {
            Mono<Task> actual = repository.getById(1L);
            StepVerifier.create(actual)
                    .expectNext(expected01)
                    .verifyComplete();
        }

        @Test
        @DisplayName("0件取得")
        void getNothing() {
            Mono<Task> actual = repository.getById(99L);
            StepVerifier.create(actual)
                    .expectNext()
                    .verifyComplete();
        }
    }

    @Nested
    class getAll {
        @Test
        @DisplayName("全件取得")
        void getAll() {
            Flux<Task> actual = repository.getAll();
            StepVerifier.create(actual)
                    .expectNext(expected01, expected02, expected03)
                    .verifyComplete();
        }
    }

    @Nested
    class update {
        @Test
        @DisplayName("1件更新")
        void updateOne() {
            Task input = new Task().toBuilder()
                    .createdOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                    .updatedOn(LocalDateTime.of(2020, 1, 1, 0, 00, 0))
                    .taskId(1L)
                    .text("更新")
                    .deadline(LocalDateTime.of(2020, 1, 1, 0, 0, 1))
                    .completed(false)
                    .deleted(false)
                    .priorityRank(0L)
                    .taskListId(1L)
                    .build();

            Mono<Integer> actual = repository.update(Mono.just(input));
            StepVerifier.create(actual)
                    .expectNext(1)
                    .verifyComplete();
        }

        @Test
        @DisplayName("0件更新")
        void updateNothing() {
            Task input = new Task().toBuilder()
                    .createdOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                    .updatedOn(LocalDateTime.of(2020, 1, 1, 0, 00, 0))
                    .taskId(99L)
                    .text("更新")
                    .deadline(LocalDateTime.of(2020, 1, 1, 0, 0, 1))
                    .completed(false)
                    .deleted(false)
                    .priorityRank(0L)
                    .taskListId(1L)
                    .build();

            Mono<Integer> actual = repository.update(Mono.just(input));
            StepVerifier.create(actual)
                    .expectNext(0)
                    .verifyComplete();
        }
    }

    @Nested
    class removeById {
        @Test
        @DisplayName("1件削除")
        void removeOne() {
            Mono<Integer> actual = repository.removeById(1L);
            StepVerifier.create(actual)
                    .expectNext(1)
                    .verifyComplete();
        }

        @Test
        @DisplayName("0件削除")
        void removeNothing() {
            Mono<Integer> actual = repository.removeById(99L);
            StepVerifier.create(actual)
                    .expectNext(0)
                    .verifyComplete();
        }
    }

}