# Microservices 
 
A sample microservice app consists of basic flow from end to end
 
 Client ==> APIGateway => ServiceRegistry => HystrixDashboard(CircuitBreaker) => UserService/DepartmentService => CloudConfigServer
 
 
ZipKin + Slueth => Distributed Tracing logs
 


## Microservices Architecture 

```
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    hostname: localhost
```




### Department Service: 
```
curl --location --request POST 'http://localhost:9001/departments/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "departmentName": "CSE",
    "departmentAddress": "Hyderabad",
    "departmentCode": "CSE_01"
}'
```

```
curl --location --request GET 'http://localhost:9001/departments/1'
```


### User Service: 

```
 curl --location --request POST 'http://localhost:9002/users/' \
 --header 'Content-Type: application/json' \
--data-raw '{
    "firstName": "Ramu",
    "lastName": "Evana",
    "email": "ramu.evana@gmail.com",
    "departmentId": "1"
}'
```

```
curl --location --request GET 'http://localhost:9002/users/1'
```



## API-GATEWAY Introduced:


#### Department Service
```
curl --location --request POST 'http://localhost:9191/departments/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "departmentName": "CSE",
    "departmentAddress": "Hyderabad",
    "departmentCode": "CSE_01"
}'
```
```
curl --location --request GET 'http://localhost:9191/departments/1'
```


#### User Service: 

```
 curl --location --request POST 'http://localhost:9191/users/' \
 --header 'Content-Type: application/json' \
--data-raw '{
    "firstName": "Ramu",
    "lastName": "Evana",
    "email": "ramu.evana@gmail.com",
    "departmentId": "1"
}'
```

```
curl --location --request GET 'http://localhost:9191/users/1'
```


## Zupkin Server : for distributed tracing 
```
http://127.0.0.1:9411
```



