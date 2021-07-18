# Distributed Tracing by Jeager

Setup jeager, it can deploy either to k8s cluster or run locally
Here we run it locally by docker.

    docker run -d --name jaeger -p 16686:16686 -p 6831:6831/udp jaegertracing/all-in-one:1.24.0

Deploy demo service

    skaffold dev
    
Demo

    kubectl port-forward service/tracing-by-jaeger 8080:8080
    
    curl http://localhost:8080/hi

        
Open jaeger UI and check trace:

    http://localhost:16686/