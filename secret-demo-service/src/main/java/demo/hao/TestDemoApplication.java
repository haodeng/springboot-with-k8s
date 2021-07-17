package demo.hao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TestDemoApplication {

    @Bean
    public TestRepository testRepository() {
        return new TestRepository();
    }

    public static void main(String[] args) {
        SpringApplication.run(TestDemoApplication.class, args);
    }
}
