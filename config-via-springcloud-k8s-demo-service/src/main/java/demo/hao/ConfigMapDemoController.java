package demo.hao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/k8s-config-map")
public class ConfigMapDemoController {

    @Autowired
    MyConfig myConfig;

    @Value("${demo_value1: default val1}")
    private String demoValue1;

    @GetMapping("/val1")
    public String getValue1() {
        return demoValue1;
    }

    @GetMapping
    public String getMsg() {
        return myConfig.getMessage();
    }

}
