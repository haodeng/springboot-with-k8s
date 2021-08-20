# 1. springboot with k8s

## Config
* k8s config map for properties (checkout config-demo-service)
* Springboot auto refresh when k8s configMap changes (checkout config-via-springcloud-k8s-demo-service)
* get k8s secret

## Service discovery
* Service discovery in k8s

## Dev mode, debug
* hot deploy to k8s via skaffold dev mode (checkout service-discovery)
* Intercept the orders-service for local debug and fast dev. No hot deploy required even (checkout service-discovery)
* Use IDE remote debug a pod
* Use tilt to quick dev & deploy

## Ingress
* Ingress demo

## Log centralization
* EFK stack (TBD://)

## Distributed Tracing
* By zipkin and sleuth
* By jeager (recommended)

## Monitor (by prometheus)
Check out: https://github.com/haodeng/techy-notes/blob/master/k8s/install-prometheus.md

# 2. Microservice patterns with k8s
In general k8s makes life much easier than traditional Microservice setup.

* Service discovery and registration

  No more Eureka. pods are exposed them as services in k8s. 
  Microservice inner communication can call service name, the embedded DNS server iin k8s will resolve the service name
  
* Load balancing

  Again k8s services already have load balancing capabilities. Service is the single entry to a set of pods.
  No more zuul, ribbon and Eureka.

* Externalized Configuration

  k8s offers configMap, secret, and these configs can be injected to container env. 
  We also need to authorize the serviceAccount to access to ConfigMaps.
  So no more config servers, no more valut server.
  
* Log centralization

  ELK is replaced by EFK stack. Fluentd is better than Logstash.
  
* Health check API
  
  Springboot Actuator expose info of running services. 
  k8s has ReadinessProbe (if a container is ready to accept traffic) and LivenessProbe (The running state of a container) can fit into springboot actuator.
  
  Example of deployment:
  
        spec:
              containers:
              - env:
                ...
                livenessProbe:
                  httpGet:
                    path: /actuator/health
                    port: 8080
                    scheme: HTTP
                  initialDelaySeconds: 90
                ...
                readinessProbe:
                  httpGet:
                    path: /actuator/health
                    port: 8080
                    scheme: HTTP
                  initialDelaySeconds: 10
        ... 
  
* API Gateway

  k8s ingress offers the external access to services in cluster. 
  Ingress is able to route the traffic to internal services.
  So the old API gateway setup can be removed.
  
* Distributed Tracing

  We still use Zipkin or Jaeger. 
  Just need to deploy zipkin to k8s cluster. 
  In application.properties, put spring.zipkin.baseUrl=http://zipkin/, this points to zipkin k8s service
  