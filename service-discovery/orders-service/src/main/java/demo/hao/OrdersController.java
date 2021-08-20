package demo.hao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @GetMapping
    public String getAll() {
        log.info("get orders.");
        return "[order1, order2, order3]";
    }
}
