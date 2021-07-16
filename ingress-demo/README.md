# Ingress
## Setup
Install Ingress Controller in Minikube
minikube may have issue adding ingress addons: https://github.com/kubernetes/minikube/issues/7332

Try:

    minikube delete
    minikube start --vm=true
    minikube addons enable ingress

Create ingress.yaml

    apiVersion: networking.k8s.io/v1
    kind: Ingress
    metadata:
      name: ingress-demo
      namespace: default
      annotations:
        nginx.ingress.kubernetes.io/rewrite-target: /$2
    spec:
      rules:
        - host: ingress-demo
          http:
            paths:
              - path: /service-a(/|$)(.*)
                pathType: Prefix
                backend:
                  service:
                    name: service-a
                    port:
                      number: 8080
              - path: /service-b(/|$)(.*)
                pathType: Prefix
                backend:
                  service:
                    name: service-b
                    port:
                      number: 8080

for nginx rewrite target: https://kubernetes.github.io/ingress-nginx/examples/rewrite/

## Demo
Setup hosts

    sudo vi /etc/hosts
    # append the line:
    127.0.0.1       ingress-demo

curl

    curl http://ingress-demo/service-b/b
    Hi from service B
    
    curl http://ingress-demo/service-a/test
    Test from service A
    
    curl http://ingress-demo/service-a  
    Hi from service A