package dev.abekoh.todo.config;

import dev.abekoh.todo.handler.TaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouteConfiguration {
    @Bean
    RouterFunction<ServerResponse> routes(TaskHandler handler) {
        return RouterFunctions.nest(RequestPredicates.path("/todo/tasks"),
                RouterFunctions.route(RequestPredicates.GET("/{taskId}"), handler::getOne));
    }
}
