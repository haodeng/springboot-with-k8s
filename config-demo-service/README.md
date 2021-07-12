# Config map
PS: The biggest problem of the approach is when k8s configMap value updated, all pods will not able to pick up the changes.

Use "Spring Cloud Kubernetes" to hot reload config map changes.


Checkout config-via-springcloud-k8s-demo-service demo


Build and push the image first

    mvn compile jib:build
    
    
Controller

    @Value("${demo_value1}")
    private String demoValue1;

    @Value("${demo_value2}")
    private String demoValue2;

application properties

    demo_value1=val1 from springboot props
    demo_value2=val2 from springboot props
    
Now put the property to config_map.yaml

    apiVersion: v1
    kind: ConfigMap
    metadata:
      name: demo-config
    data:
      demo_value1: val1 from k8s config map
      demo_value2: val2 from k8s config map

## Deploy

    kubectl apply -f config_map.yaml
    
    kubectl create service clusterip config-demo-service --tcp=8080:8080 --dry-run=client -o=yaml > service.yaml
    kubectl apply -f service.yaml
    
    kubectl create deployment config-demo-service --image=haodeng/config-demo-service:latest --dry-run='client' --output='yaml' > deployment.yaml

Add env vars to the deployment.yaml under containers

    ...
    spec:
          containers:
          - image: haodeng/config-demo-service:latest
            name: config-demo-service
            resources: {}
            env:
              - name: DEMO_VALUE1
                valueFrom:
                  configMapKeyRef:
                    name: demo-config
                    key: demo_value1
              - name: DEMO_VALUE2
                valueFrom:
                  configMapKeyRef:
                    name: demo-config
                    key: demo_value2
    ...

Apply

    kubectl apply -f deployment.yaml
    
Demo

    curl http://localhost:8080/k8s-config-map/val1
    val1 from k8s config map
    
    curl http://localhost:8080/k8s-config-map/val2
    val2 from k8s config map
All values should take from demo-config configMap from k8s

