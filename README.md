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
 API-GATEWAY	       - 192.168.1.1:API-GATEWAY:9191
 CONFIG-SERVER	     - 192.168.1.1:CONFIG-SERVER:9296 
 HYSTRIX-DASHBOARD	 - 192.168.1.1:HYSTRIX-DASHBOARD:9295
 
 PRODUCT-SERVICE	  - 192.168.1.1:PRODUCT-SERVICE:9001
 CUSTOMER-SERVICE	 - 192.168.1.1:CUSTOMER-SERVICE:9002
 ORDER-SERVICE	    - 192.168.1.1:ORDER-SERVICE:9005
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

<img width="1675" alt="image" src="https://user-images.githubusercontent.com/23380019/172650135-dbc58a20-ffb5-4ffb-8a18-f0bd725fc39b.png">


#### HystrixDashboard
 
Generally when microservices scalling much more, when requests are spanning from one to other then it is very common that any of the services can go down and causes other dependent services wait longer than expected and cause inconvience, it was very difficult to debug such scenarios where was the failure, hence somewhere we need break the loop saying "This Service is tempararly down", this is called as Circuite Breaker design principle in micirsoervice architecture. This can be achieved from Hystrix service.

URL: http://localhost:9295/hystrix

<img width="1677" alt="image" src="https://user-images.githubusercontent.com/23380019/172650307-64927729-fcec-41fb-9063-15cada8922ef.png">


#### ConfigServerCloud
 
This service is basically used to make all common properties or configuration info to once place and maintain in any repository and make it secure with OAuth enabled authentication, now a days it is pretty similar to vault/secret manager service in cloud.


### How can I try this in my local

- Clone the project
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


## Registering microservices(product, customer and order) with ServiceRegistry

This service exposes one dashboard (Eureka Dashboard) to see all active registered services
Next step is to make this servicd and up and all other services make them as Eureka clients



## API-GATEWAY Introduced:

With API gateway we can directly call single host IP, then this sevice can redirects required based on resource URL to correpsonding service with defined routes 

```
server:
  port: 9191
spring:
  application:
    name: API-GATEWAY
  cloud:
    gateway:
      routes:
        - id: CUSTOMER-SERVICE
          uri: lb://CUSTOMER-SERVICE
          predicates:
            - Path=/customers/**
          filters:
            - name: CircuitBreaker
              args:
                name: CUSTOMER-SERVICE
                fallbackuri: forward:/customerServiceFallback

        - id: PRODUCT-SERVICE
          uri: lb://PRODUCT-SERVICE
          predicates:
            - Path=/products/**
          filters:
            - name: CircuitBreaker
              args:
                name: PRODUCT-SERVICE
                fallbackuri: forward:/productServiceFallback

        - id: ORDER-SERVICE
          uri: lb://ORDER-SERVICE
          predicates:
            - Path=/orders/**
          filters:
            - name: CircuitBreaker
              args:
                name: ORDER-SERVICE
                fallbackuri: forward:/orderServiceFallback
hystrix:
  command:
    fallbackcmd:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 4000
management:
  endpoints:
    web:
      exposure:
        include: hystrix.stream

```

![Uploading image.png…]()


### Product Service:

To Save product
```
curl --location --request POST 'http://localhost:9191/products/' \
--header 'Content-Type: application/json' \
--data-raw '{ 
 "productName": "Mobile",
 "productAddress": "Narsapur",
 "productCode": "MOB_101",
 "price": 34000,
 "category": "MOBILES"
}'
```

To get product
```
curl --location --request GET 'http://localhost:9191/products/1'
```


### Customer Service:

To save customer
```
 curl --location --request POST 'http://localhost:9191/customers/' \
 --header 'Content-Type: application/json' \
--data-raw '{
    "firstName": "Ramu",
    "lastName": "Evana",
    "email": "ramu.evana@gmail.com"
}'
```

To get customer
```
curl --location --request GET 'http://localhost:9191/customers/1'
```

### Order Service:

Get save order
```
 curl --location --request POST 'http://localhost:9191/orders/' \
 --header 'Content-Type: application/json' \
--data-raw '{
    "customerId": "1",
    "productId": "1",
    "location": "Hyderabad"
}'
```

To get order
```
curl --location --request GET 'http://localhost:9191/orders/1'
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


