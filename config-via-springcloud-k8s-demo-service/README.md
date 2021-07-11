# Auto refresh K8s configMap
The biggest problem of previous demo (config-demo-service) is when k8s configMap changes, we have to restart springboot to reload changes.

"Spring Cloud Kubernetes Configuration Watcher" comes to solve the issue.

PS: PropertySource Reload has been deprecated in the 2020.0 release. 
Use Spring Cloud Kubernetes Configuration Watcher instead.
https://docs.spring.io/spring-cloud-kubernetes/docs/current/reference/html/index.html#propertysource-reload

## Springboot setup/config

Add

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-kubernetes-client-config</artifactId>
        </dependency>
        


Please be aware that when spring-cloud-kubernetes-config is on the classpath, spring.cloud.kubernetes.enabled should be set in bootstrap.{properties|yml}
https://docs.spring.io/spring-cloud-kubernetes/docs/current/reference/html/index.html#kubernetes-ecosystem-awareness


In bootstrap.ymal:

    spring:
      cloud:
        kubernetes:
          config:
            enabled: true
            # The name of the configMap in k8s to load
            name: reload-demo-config
            
In application.yaml:

    spring:
      application:
        name: config-via-springcloud-k8s-demo-service
      cloud:
        kubernetes:
          reload:
            enabled: true
            mode: event

Setup a ConfigurationProperties for k8s configMap inject:

    @Data
    @Configuration(proxyBeanMethods = false)
    @ConfigurationProperties(prefix = "bean")
    public class MyConfig {
    
        private String message = "msg from app";
        
Build and publish image:

    mvn compile jib:build
                
## k8s setups
configMap

    apiVersion: v1
    kind: ConfigMap
    metadata:
      name: reload-demo-config
      labels:
        spring.cloud.kubernetes.config: "true"
    data:
      "demo_value1": "val1 from k8s config map"
      application.properties: |-
        bean.message=Hello from k8s!
        bean.greeting=Greeting from k8s!

Security config, checkout k8s/roles_for_access.yaml

For deployment and service, checkout k8s/deployment.yaml and k8s/sservice.yaml

## Demo

    kubectl apply -f roles_for_access.yaml
    kubectl apply -f config_map.yaml
    kubectl apply -f deployment.yaml
    kubectl apply -f service.yaml
    
    kubectl port-forward service/config-via-springcloud-k8s-demo-service 8080:8080
    
    curl http://localhost:8080/k8s-config-map
    Hello from k8s!
    
    curl http://localhost:8080/k8s-config-map/val1
    val1 from k8s config map

Change configMap values to "2.0":

    apiVersion: v1
        kind: ConfigMap
        metadata:
          name: reload-demo-config
          labels:
            spring.cloud.kubernetes.config: "true"
        data:
          "demo_value1": "val1 2.0 from k8s config map"
          application.properties: |-
            bean.message=Hello 2.0 from k8s!
            bean.greeting=Greeting 2.0 from k8s!

The pod log should print:
    
    kubectl logs -f <pod_name>

    2021-07-11 10:54:43.981  INFO 1 --- [pool-3-thread-1] sClientEventBasedConfigMapChangeDetector : ConfigMap reload-demo-config was added.
    2021-07-11 10:54:43.994  INFO 1 --- [pool-3-thread-1] sClientEventBasedConfigMapChangeDetector : Configuration change detected, reloading properties.
    2021-07-11 10:54:43.994  INFO 1 --- [pool-3-thread-1] sClientEventBasedConfigMapChangeDetector : Reloading using strategy: REFRESH
This proves the springboot has reloaded the configMap

Test:

    curl http://localhost:8080/k8s-config-map
    Hello 2.0 from k8s!
        
    curl http://localhost:8080/k8s-config-map/val1
    val1 from k8s config map

Only MyConfig.message changed to "Hello 2.0 from k8s!". ConfigurationProperties supports auto refresh.

"val1 from k8s config map" is injected via env in deployment.yaml, env properties seems not able to auto refresh yet.


