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

    @Autowired
    private DiscoveryClient discoveryClient;
    
    @GetMapping
    public String getOrders() {
        List<ServiceInstance> ordersServices = discoveryClient.getInstances("orders-service");
        if (ordersServices != null && !ordersServices.isEmpty()) {
            ServiceInstance ordersService = ordersServices.get(0);
            String url = "http://orders-service:" + ordersService.getPort() + "/orders";
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
         ...   
k8s itself knows how to locate orders-service, port can be find by discoveryClient. 
Demo only how discoveryClient get a service instance info, calling orders-service in prod should via a "API gateway".

Get all services registered

    @GetMapping("/services")
        public List<String> getServices() {
            return discoveryClient.getServices();
        }
        
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

## Intercept the orders-service for fast dev (No hot deploy required)
Use Telepresence

The purpose is to break the discovery-client-demo's dependency on orders-service in k8s env, by intercepting the orders-service traffic and redirect to local port

Check the services ready for intercept:

    telepresence list
    orders-service: ready to intercept (traffic-agent not yet installed)
    discovery-client-demo: ready to intercept (traffic-agent not yet installed)
    

Intercept orders-service, all traffics to orders-service will redirect to localhost:8080:

    telepresence intercept orders-service --port 8080

Start my local orders-service, eg: from IDE in debug mode, set up break point

Test:

    minikube service discovery-client-demo
    
    # This opens the browser and hit http://127.0.0.1:51186/
    [order1, order2, order3]

The breakpoint should expect working, and we are able to debug the orders-service

Change the Order service code locally:

    @GetMapping
    public String getAll() {
        return "[order1, order2, order3, new order4]";
    }

Restart the orders-service in IDE, or enable live load. Refresh http://127.0.0.1:51186/

    [order1, order2, order3, new order4] 

Change expected, no hot deploy needed. local debug and dev becomes much easier.

Clean up

    telepresence leave orders-service