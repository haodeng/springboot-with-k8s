# Distributed Tracing by Jeager

Setup jeager, it can deploy either to k8s cluster or run locally
Here we run it locally by docker.

    docker run -d --name jaeger -p 16686:16686 -p 6831:6831/udp jaegertracing/all-in-one:1.24.0

Add dependency:

        <dependency>
            <groupId>io.opentracing.contrib</groupId>
            <artifactId>opentracing-spring-jaeger-cloud-starter</artifactId>
            <version>3.3.1</version>
        </dependency>
        
Add to application.yml:

    opentracing:
      jaeger:
        udp-sender:
          # if deployed to k8s, use the jaeger k8s service name
          host: 192.168.1.25
          port: 6831
          
Deploy demo service

    skaffold dev
    
Demo

    kubectl port-forward service/tracing-by-jaeger 8080:8080
    
    curl http://localhost:8080/hi

        
Open jaeger UI and check trace:

    http://localhost:16686/