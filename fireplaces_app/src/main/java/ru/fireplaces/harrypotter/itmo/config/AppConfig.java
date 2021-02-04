package ru.fireplaces.harrypotter.itmo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Executor;

/**
 * App config class.
 *
 * @author seniorkot
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    /**
     * Adds interceptors into the application.
     *
     * @param registry InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor());
    }

    /**
     * Async task executor bean.
     *
     * @return {@link ThreadPoolTaskExecutor} object
     */
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setTaskDecorator(...);
        executor.initialize();
        return executor;
    }
}
