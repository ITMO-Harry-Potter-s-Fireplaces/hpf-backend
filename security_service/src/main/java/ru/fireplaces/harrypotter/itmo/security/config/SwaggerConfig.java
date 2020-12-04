package ru.fireplaces.harrypotter.itmo.security.config;

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
                .groupName("hpf_security_service")
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.fireplaces.harrypotter.itmo.security"))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(new ApiInfoBuilder()
                        .title("HPF Security Service")
                        .description("Harry Potter's Fireplaces security service API v.1.0")
                        .build()
                );
    }
}

