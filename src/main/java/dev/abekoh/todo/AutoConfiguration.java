package dev.abekoh.todo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@EnableR2dbcRepositories
public class AutoConfiguration {
    @Bean
    RouterFunction<ServerResponse> routes(TodoHandler todoHandler) {
        return RouterFunctions.route(
                RequestPredicates.GET("/task/{taskId}"),
                todoHandler::get);
    }
}
