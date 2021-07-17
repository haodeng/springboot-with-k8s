# Secret
Demo get user/pass from secret, connect to mysql and get db time


    kubectl create secret generic mysql-demo-secret \
    --from-literal=mysql.username=test \
    --from-literal=mysql.password=test \
    --dry-run=client -o=yaml
    
    apiVersion: v1
    data:
      mysql.password: dGVzdA==
      mysql.username: dGVzdA==
    kind: Secret
    metadata:
      creationTimestamp: null
      name: mysql-demo-secret

secret will be injected to deployment env:

           env:
            - name: MYSQL_USERNAME
              valueFrom:
                secretKeyRef:
                  name: mysql-demo-secret
                  key: mysql.username
            - name: MYSQL_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mysql-demo-secret
                  key: mysql.password
                  
Deploy

    skaffod dev
    

Demo

    kubectl port-forward service/secret-demo-service 8080:8080
    
    curl http://localhost:8080/time
    2021-07-17 18:11:07

As expected, connect to local mysql db and get the db time
