# Microservices 
###### An Anji Evana self prepared document
 
A sample microservice app consists of basic flow from end to end

```
Client ==> 
  Gateway-Service => 
   ServiceRegistry =>
    ResielenceDashboard(CircuitBreaker)=> 
      CustomerService/OrderService/ProductService => 
        ConfigServerCloud
```
 
Logging:
Splunk(ELK) + ZipKin + Slueth => Distributed Tracing logs

Security:
Pending: Adding OAuth2 UAA to each service will help

![image](https://user-images.githubusercontent.com/23380019/171115561-9695febe-7be6-4a46-a033-8e50a10abc85.png)


## Description 

Total Number of services defined here 

```
 API-GATEWAY	  - 192.168.22.30:API-GATEWAY:9191
 CONFIG-SERVER	  - 192.168.22.30:CONFIG-SERVER:9296
 CUSTOMER-SERVICE	  - 192.168.22.30:CUSTOMER-SERVICE:9002
 HYSTRIX-DASHBOARD	 - 192.168.22.30:HYSTRIX-DASHBOARD:9295
 ORDER-SERVICE	  - 192.168.22.30:ORDER-SERVICE:9005
 PRODUCT-SERVICE	  - 192.168.22.30:PRODUCT-SERVICE:9001
```

#### Product Service:
 
This service exposes few APIs to save products and get product with ID

```
curl --location --request POST 'http://localhost:9001/products/' \
--header 'Content-Type: application/json' \
--data-raw '{ 
 "productName": "Mobile",
 "productAddress": "Narsapur",
 "productCode": "MOB_101",
 "price": 34000,
 "category": "MOBILES"
}'


curl --location --request GET 'http://localhost:9001/products/1'
```

#### Customer Service:
 
This service exposes few APIs to save customer with cust_id and get customer with customer info 

```
 curl --location --request POST 'http://localhost:9002/customers/' \
 --header 'Content-Type: application/json' \
--data-raw '{
    "firstName": "Ramu",
    "lastName": "Evana",
    "email": "ramu.evana@gmail.com"
}'

curl --location --request GET 'http://localhost:9002/customers/1'
```

#### Order Service:
 
This service exposes few APIs to get order info along with customer info and product info by calling above services

```
curl --location --request POST 'http://localhost:9005/orders/' \
 --header 'Content-Type: application/json' \
--data-raw '{
    "customerId": "1",
    "productId": "1",
    "location": "Hyderabad"
}'


curl --location --request GET 'http://localhost:9005/orders/1'
```

#### ServiceRegistry (Discovery Service Design Pattern)
 
This service is EurekaServer so that all other services can register with services, so that all other services become Eureka clients for this service, meaning every other service need @EnableEurekaClient and this defined @EnableEurekaServer

URL: http://localhost:8761/

![Uploading image.png…]()


#### HystrixDashboard
 
Generally when microservices scalling much more, when requests are spanning from one to other then it is very common that any of the services can go down and causes other dependent services wait longer than expected and cause inconvience, it was very difficult to debug such scenarios where was the failure, hence somewhere we need break the loop saying "This Service is tempararly down", this is called as Circuite Breaker design principle in micirsoervice architecture. This can be achieved from Hystrix service.

URL: http://localhost:9295/hystrix

![Uploading image.png…]()


#### ConfigServerCloud
 
This service is basically used to make all common properties or configuration info to once place and maintain in any repository and make it secure with OAuth enabled authentication, now a days it is pretty similar to vault/secret manager service in cloud.


### How can I try this in my local

- Clone the project
- 
```
git clone git@github.com:dcs-admin/microservices.git
```

- Open all repos in individual IDEs either IntelliJ/Eclipse

- Try to build and run individual service
```
mvn clean install
mvn package
mvn springboot:run
```



- Open following Dashboards to see the services up or not

```
http://localhost:8761/  -->> Eureka Dashboard: ServiceRegistry
http://127.0.0.1:9411/zipkin/ --> Zipkin Server: Log Tracing
http://localhost:9191/actuator/hystrix.stream --> Enable HystrixStream to check CircuitBreaker functionality
http://localhost:9295/hystrix/monitor  --> Hystrix Dashboard: Add above URL here to stream the status 

```

### Eureka Configuration 

General config

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

<img width="1435" alt="image" src="https://user-images.githubusercontent.com/23380019/171010867-31d94a95-c99a-4a9a-ad47-b2dd86c238bf.png">



## Calling individual serivces : user and department

### Department Service: Handy commands 

To save dept
```
curl --location --request POST 'http://localhost:9001/departments/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "departmentName": "CSE",
    "departmentAddress": "Hyderabad",
    "departmentCode": "CSE_01"
}'
```

To get dept
```
curl --location --request GET 'http://localhost:9001/departments/1'
```


### User Service: Handy commands 

To save user 
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

To get user along with department info
```
curl --location --request GET 'http://localhost:9002/users/1'
```

Response
```
{
   "user":{"userId":3,"firstName":"Ramu","lastName":"Evana","email":"ramu.evana@gmail.com","departmentId":1},
   "department":{"departmentId":1,"departmentName":"CSE","departmentAddress":"Hyderabad","departmentCode":"CSE_01"}
}
```

<img width="1337" alt="image" src="https://user-images.githubusercontent.com/23380019/171011046-6891cc6f-f34e-411c-90f6-b8e92d02bd2d.png">



## Registering microservices(user and dept) with ServiceRegistry

This service exposes one dashboard (Eureka Dashboard) to see all active registered services
Next step is to make this servicd and up and all other services make them as Eureka clients



## API-GATEWAY Introduced:

With API gateway we can directly call single host IP, then this sevice can redirects required based on resource URL to correpsonding service with defined routes 

```
cloud:
    gateway:
      routes:
        - id: USER-SERVICE
          uri: lb://USER-SERVICE
          predicates:
            - Path=/users/**
          filters:
            - name: CircuitBreaker
              args:
                name: USER-SERVICE
                fallbackuri: forward:/userServiceFallback
        - id: DEPARTMENT-SERVICE
          uri: lb://DEPARTMENT-SERVICE
          predicates:
            - Path=/departments/**
          filters:
            - name: CircuitBreaker
              args:
                name: DEPARTMENT-SERVICE
                fallbackuri: forward:/departmentServiceFallback

```

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


## Hystrix Dashboard to see the health of the services

- Call following on browser , so that hystrix stream can be enabled
```
http://localhost:9191/actuator/hystrix.stream
```

- Next if you call see hystrix dashboard, you can see the status of the service


<img width="1446" alt="image" src="https://user-images.githubusercontent.com/23380019/171011696-1130b554-17a1-4c65-a1fd-28e00d858dac.png">

<img width="1454" alt="image" src="https://user-images.githubusercontent.com/23380019/171012009-118a3f3e-4abe-491e-912f-927cd3251a47.png">



## Zupkin Server : for distributed tracing 

For both user and dept services add following properties will push the logs to distributed ZipKin server

```
spring: 
  zipkin:
    url: http://127.0.0.1:9411
```


- Download the latest released jar from zipkin website 
- start the server 
```
java -jar zipkin.jar
```

- Restart all above services 
- execute ./unitTest.sh
- Now you can go to Zipkin dashboard and check the traces and play around there

<img width="1418" alt="image" src="https://user-images.githubusercontent.com/23380019/171010969-b0fd2d03-3600-48ca-8b17-92fadab409cb.png">


