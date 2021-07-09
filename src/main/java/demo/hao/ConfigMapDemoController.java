package demo.hao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/k8s-config-map")
public class ConfigMapDemoController {

    @Value("${demo_value1}")
    private String demoValue1;

    @Value("${demo_value2}")
    private String demoValue2;

    @GetMapping("/val1")
    public String getValue1() {
        return demoValue1;
    }

    @GetMapping("/val2")
    public String getValue2() {
        return demoValue2;
    }
}
