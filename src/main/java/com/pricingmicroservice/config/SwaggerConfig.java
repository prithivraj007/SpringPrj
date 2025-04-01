package com.pricingmicroservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pricing Microservice API")
                        .version("1.0")
                        .description("API documentation for the Pricing Microservice")
                        .contact(new Contact().name("Your Name").url("https://yourwebsite.com").email("your@email.com")));
    }
}
