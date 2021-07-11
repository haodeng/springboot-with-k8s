package demo.hao;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "bean")
public class MyConfig {

    private String message = "msg from app";
    private String greeting = "greeting from app";

}
