package br.com.forum.infra.docs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Value("${api.server.url.dev}")
    private String devUrl;
    @Value("${api.server.url.prod}")
    private String prodUrl;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Forum-Hub")
                        .description("Challenge Oracle Next Education + Alura")
                        .version("v1")
                        .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT"))
                        .contact(new Contact().name("maxjdev").email("maxjramosdev@gmail.com").url("https://www.linkedin.com/in/maxjdev/")))
                .servers(List.of(new Server().url(devUrl).description("URL do servidor em ambiente de desenvolvimento"),
                        new Server().url(prodUrl).description("URL do servidor em ambiente de produção")));
    }
}
