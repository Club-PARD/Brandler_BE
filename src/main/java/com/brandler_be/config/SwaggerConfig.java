package com.brandler_be.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Swagger(OpenAPI) 설정 클래스
 * API 문서화를 위한 Swagger 설정을 정의합니다.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Server httpsServer = new Server();
        httpsServer.setUrl("https://brandler.shop");
        httpsServer.setDescription("Production HTTPS server");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development server");

        return new OpenAPI()
                .components(new Components())
                .servers(Arrays.asList(httpsServer, localServer))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("BRANDLER API Documentation")
                .description("brandler API 문서입니다.")
                .version("1.0.0");
    }
}