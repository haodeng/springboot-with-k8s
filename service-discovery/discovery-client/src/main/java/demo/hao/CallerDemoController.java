package demo.hao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/")
public class CallerDemoController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping
    public String getOrders() {
        List<ServiceInstance> ordersServices = discoveryClient.getInstances("orders-service");
        if (ordersServices != null && !ordersServices.isEmpty()) {
            ServiceInstance ordersService = ordersServices.get(0);
            String url = "http://orders-service:" + ordersService.getPort() + "/orders";
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

            return responseEntity.getBody();
        }

        return "N/A";
    }

    @GetMapping("/services")
    public List<String> getServices() {
        return discoveryClient.getServices();
    }
}
