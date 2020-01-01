package dev.abekoh.todo.config;

import dev.abekoh.todo.handler.TaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouteConfiguration {
    @Bean
    RouterFunction<ServerResponse> routes(TaskHandler handler) {
        return nest(path("/todo/tasks"),
                route(GET("/{taskId}"), handler::getOne)
                        .andRoute(GET("/"), handler::getAll)
                .andRoute(POST("/"), handler::addOne)
                .andRoute(PATCH("/"), handler::updateOne)
                .andRoute(DELETE("/{taskId}"), handler::removeOne));
    }
}
