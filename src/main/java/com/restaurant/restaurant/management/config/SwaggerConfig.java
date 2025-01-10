package com.restaurant.restaurant.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(
                        new Info()
                                .title("API Restaurante")
                                .description("Documentacion interactiva de la API de Restaurante")
                                .version("1.0")
                                .contact(new Contact()
                                        .name("Equipo de desarrollo Dani")
                                        .email("daniela.serrano.dev@gmail.com")
                                )
                );
    }
}
