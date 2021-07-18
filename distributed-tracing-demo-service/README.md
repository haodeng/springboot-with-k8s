# Use Sleuth and Zipkin

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-sleuth</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zipkin</artifactId>
            <version>2.2.8.RELEASE</version>
        </dependency>

Deploy Zipkin to k8s

    kubectl create deployment zipkin --image=openzipkin/zipkin
    kubectl expose deployment zipkin --type=NodePort --port 9411
    
In application.yml, define zipkin url:

    spring:
      application:
        name: distributed-tracing-demo-service
      sleuth:
        sampler:
          probability: 1
      zipkin:
        baseUrl: http://zipkin:9411/

Demo

    kubectl port-forward service/distributed-tracing-demo-service 8080:8080
    curl http://localhost:8080/hi
    
    # check trace
    minikube service zipkin
