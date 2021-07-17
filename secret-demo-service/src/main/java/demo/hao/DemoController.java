package demo.hao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private TestRepository testRepository;

    @GetMapping("/time")
    public String getValue1() {
        return testRepository.getDbTime();
    }


}
