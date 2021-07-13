# Service Discovery
In k8s, unlike traditional microservice architecture, we do not need to setup a discovery server (Eureka, Consul).

The k8s service is already exposing the access point to the pods.

We only need to define the discovery client. This client lets you query Kubernetes endpoints (see services) by name.

## Discovery client

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-kubernetes-fabric8</artifactId>
    </dependency>

Add @EnableDiscoveryClient

    @SpringBootApplication
    @EnableDiscoveryClient
    public class DiscoveryClientDemoApplication {
    
In demo controller, call orders-service:

    @GetMapping
    public String getOrders() {
        String url = "http://orders-service:8080/orders";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        
        return responseEntity.getBody();
    }
k8s itself knows how to access orders-service

Deploy

    skaffold run

## Order service
Setup a simple service for discovery-client-demo to call

Deploy

    skaffold run

## Demo

    minikube service discovery-client-demo
    
    a browser opens hit http://127.0.0.1:59701/
    [order1, order2, order3]
The discovery-client-demo calls http://orders-service:8080/orders works

## Demo of hot deploy orders-service

    skaffold dev

Change OrdersController returns more orders

    @GetMapping
        public String getAll() {
            return "[order1, order2, order3, order4]";
        }

Rebuild

    mvn clean install
    
Check out the skaffold terminal, it starts to auto build image and deploy to k8s

Verify deploy
    
    minikube service discovery-client-demo
        
    a browser opens hit http://127.0.0.1:<random_port>/
    [order1, order2, order3, order4]

Clean up

Ctrl+C skaffold terminal, auto clean up

    ^CCleaning up...
     - service "orders-service" deleted
     - deployment.apps "orders-service" deleted
