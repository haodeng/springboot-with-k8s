package demo.hao;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceAController {

    @GetMapping
    public String hi() {
        return "Hi from service A";
    }

    @GetMapping("/test")
    public String test() {
        return "Test from service A";
    }
}
