package com.icms.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.context.annotation.Bean;

@Configuration 
public class TestRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .GET("/test", 
                    request -> ServerResponse.ok().bodyValue("Test route"))
                .build();
    }
    

}
