package ru.otus.hw;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.service.TestRunnerService;

@Configuration
@ComponentScan
@RequiredArgsConstructor
public class Application {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(Application.class);

        TestRunnerService testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();
    }
}