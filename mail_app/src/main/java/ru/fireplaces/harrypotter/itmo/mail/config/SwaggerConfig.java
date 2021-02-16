package ru.fireplaces.harrypotter.itmo.mail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger config class.
 *
 * @author seniorkot
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("hpf_mail_application")
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.fireplaces.harrypotter.itmo.mail"))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(new ApiInfoBuilder()
                        .title("Harry Potter's Mail Application")
                        .description("Harry Potter's Mail Application API v.1.0")
                        .build()
                );
    }
}

