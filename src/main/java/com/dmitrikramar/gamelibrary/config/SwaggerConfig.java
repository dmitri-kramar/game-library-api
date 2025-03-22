package com.dmitrikramar.gamelibrary.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String DESCRIPTION = """
            This RESTful API manages a video game library. Unauthenticated users can access only the /register and\s
            /login endpoints. Authenticated users can view the list of games, developers, genres, and platforms,\s
            as well as detailed entries for each. Users can view and update their profile, change their password,\s
            or delete their account. Administrators can manage all users and have full control over the entities\s
            (games, developers, genres, platforms), including the ability to add, update, and delete them. To test\s
            protected endpoints, click "Authorize" and enter the following credentials:
           \s
            — admin (for both login and password) \n
            — user (for both login and password)
           \s""";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Game Library API")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Dmitri Kramar")
                                .email("dmitri.kramar@outlook.com")
                                .url("https://dmitri-kramar.github.io/"))
                        .description(DESCRIPTION))
                .addSecurityItem(new SecurityRequirement()
                        .addList("basicAuth"))
                .components(new Components()
                        .addSecuritySchemes("basicAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")));
    }
}
