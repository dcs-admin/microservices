# Microservices  
 
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
 SERVICE-REGISTRY   - http://localhost:8761
 
 API-GATEWAY	       - 192.168.1.1:API-GATEWAY:9191
 CONFIG-SERVER	     - 192.168.1.1:CONFIG-SERVER:9296 
 HYSTRIX-DASHBOARD	 - 192.168.1.1:HYSTRIX-DASHBOARD:9295
 
 PRODUCT-SERVICE	  - 192.168.1.1:PRODUCT-SERVICE:9001
 CUSTOMER-SERVICE	 - 192.168.1.1:CUSTOMER-SERVICE:9002
 ORDER-SERVICE	    - 192.168.1.1:ORDER-SERVICE:9005
```


##### How can I start these services

```
cd service-registry && mvn spring-boot:run 

cd api-gateway && mvn spring-boot:run 

cd cloud-config-server && mvn spring-boot:run 

cd product-service && mvn spring-boot:run 

cd customer-service && mvn spring-boot:run 

cd order-service && mvn spring-boot:run 

cd auth-service && mvn spring-boot:run 

cd kafka-demo  -- optional

cd hystrix-dashboard && mvn spring-boot:run  -- optional
```

Then open Service registry URL to make sure all services up and running 
http://localhost:8761


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

<img width="1680" alt="image" src="https://user-images.githubusercontent.com/23380019/172650983-a9d235f0-3de7-42d8-8cdb-603bf8cf43f5.png">

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

<img width="1680" alt="image" src="https://user-images.githubusercontent.com/23380019/172651338-2dd8eb64-a168-489e-b389-76e44159db67.png">

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

<img width="1680" alt="image" src="https://user-images.githubusercontent.com/23380019/172651567-5af1b347-dfc3-49a5-9df6-b4bbeb42898d.png">


#### ServiceRegistry (Discovery Service Design Pattern)
 
This service is EurekaServer so that all other services can register with services, so that all other services become Eureka clients for this service, meaning every other service need @EnableEurekaClient and this defined @EnableEurekaServer

<img width="1675" alt="image" src="https://user-images.githubusercontent.com/23380019/172650135-dbc58a20-ffb5-4ffb-8a18-f0bd725fc39b.png">


#### HystrixDashboard
 
Generally when microservices scalling much more, when requests are spanning from one to other then it is very common that any of the services can go down and causes other dependent services wait longer than expected and cause inconvience, it was very difficult to debug such scenarios where was the failure, hence somewhere we need break the loop saying "This Service is tempararly down", this is called as Circuite Breaker design principle in micirsoervice architecture. This can be achieved from Hystrix service.

URL: http://localhost:9295/hystrix

<img width="1680" alt="image" src="https://user-images.githubusercontent.com/23380019/172651924-afdc495a-67d2-4360-a918-3121dd4c7ae0.png">


#### ConfigServerCloud
 
This service is basically used to make all common properties or configuration info to once place and maintain in any repository and make it secure with OAuth enabled authentication, now a days it is pretty similar to vault/secret manager service in cloud.

```
server:
  port: 9296
spring:
  application:
    name: CONFIG-SERVER
  cloud:
    config:
      server:
        git:
          uri: https://github.com/dcs-admin/config-server
          clone-on-start: true
```

<img width="1680" alt="image" src="https://user-images.githubusercontent.com/23380019/172652106-1a472d28-1a84-4e04-bf00-cde34bc54321.png">


If you see the cloud config server on mentioned above git repo, the config look like this

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
mvn spring-boot:run
```

- Open following Dashboards to see the services up or not

```
http://localhost:8761/  -->> Eureka Dashboard: ServiceRegistry
http://127.0.0.1:9411/zipkin/ --> Zipkin Server: Log Tracing
http://localhost:9191/actuator/hystrix.stream --> Enable HystrixStream to check CircuitBreaker functionality
http://localhost:9295/hystrix/monitor  --> Hystrix Dashboard: Add above URL here to stream the status 

```

## Monitor registered microservices(product, customer, order and others) with ServiceRegistry(Eureka Dashboard)

This service exposes Eureka Dashboard to see all active registered services
Next step is to make this service and up and all other services make them as Eureka clients

Here is the URL 
 http://localhost:8761/


<img width="1481" alt="image" src="https://user-images.githubusercontent.com/23380019/172652346-c90da678-b58b-4fdc-8dc4-17d9991d1085.png">


## Why API-GATEWAY 

With all above individual microservices, every service have thier own endpoints and exposed to public.

##### Drawback:
There is security vulnerability due to public port exposing and maintaining all service specific endpoints are really hectic to the customers. 

With API gateway we can directly call single host IP/domain name, then this sevice can redirects HTTP requests based on resource URL to correpsonding service with defined routes. See following yaml that describes the routing mechanism.

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

<img width="1487" alt="image" src="https://user-images.githubusercontent.com/23380019/172653268-2dc19741-5cd8-4cac-b28f-2756b3ca63e6.png">


After adding API Gateway service, then calling endpoints with single IP(http://localhost:9191) for all services which are registered with service-registry as EurekaClients as follows.

#### Product Service:

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

#### Customer Service:

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

#### Order Service:

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

The response would be like this

```
{
   "order":{
      "orderId":1,
      "customerId":1,
      "productId":1,
      "orderDate":"2022-05-31T10:54:30.833+00:00",
      "location":"Hyderabad",
      "expectedDelivery":"1970-01-01T00:00:00.006+00:00",
      "rating":0
   },
   "product":{
      "productId":1,
      "productName":"Mobile",
      "productAddress":"Narsapur",
      "productCode":"MOB_101",
      "price":34000,
      "category":"MOBILES"
   },
   "customer":{
      "customerId":1,
      "firstName":"Ramu",
      "lastName":"Evana",
      "email":"ramu.evana@gmail.com"
   },
   "responseCode":200,
   "responseMessage":null
}
```

<img width="1488" alt="image" src="https://user-images.githubusercontent.com/23380019/172653699-deabe167-256f-43d3-9473-0e5dced738dc.png">



## Hystrix Dashboard to see the health of the services

- Call following on browser , so that hystrix stream can be enabled
```
http://localhost:9191/actuator/hystrix.stream
```

- Next if you call see hystrix dashboard, you can see the status of the service


<img width="1446" alt="image" src="https://user-images.githubusercontent.com/23380019/171011696-1130b554-17a1-4c65-a1fd-28e00d858dac.png">

<img width="1454" alt="image" src="https://user-images.githubusercontent.com/23380019/171012009-118a3f3e-4abe-491e-912f-927cd3251a47.png">



## Zupkin Server : For distributed tracing 

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

<img width="1487" alt="image" src="https://user-images.githubusercontent.com/23380019/172654018-8f070682-b2a5-402c-95f9-0356ad94b771.png">

<img width="1498" alt="image" src="https://user-images.githubusercontent.com/23380019/172654859-91531921-4230-4e16-b9fd-c30353d05b19.png">

<img width="1479" alt="image" src="https://user-images.githubusercontent.com/23380019/172655080-1c87df83-a95c-4ae5-a1b1-b7aa63a3f23f.png">


#### kafka-demo

Install Kafka
https://hevodata.com/learn/install-kafka-on-mac/ ==> https://kafka.apache.org/downloads

tar -xzf kafka_2.13-3.0.0.tgz 

Current Directory:
/Users/vevana/kafka/kafka_2.13-3.7.0

Zookeeper:
./bin/zookeeper-server-start.sh config/zookeeper.properties


Kafka server:
./bin/kafka-server-start.sh config/server.properties

Create a topic:
./bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic my-topic

List Topics
./bin/kafka-topics.sh --list --bootstrap-server localhost:9092

Producer:
./bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic my-topic

Consumer:
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my-topic --from-beginning


Window1:

CURL
```
curl --location 'http://localhost:8080/push/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "customerId": 11,
    "firstName": "Revi",
    "lastName": "Hyderabad",
    "email": "revi.e@hotmail.com"
}'
```

Window2

```

2024-10-09T16:32:27.262+05:30  INFO 46474 --- [kafka-demo] [at-thread | foo] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-foo-1, groupId=foo] Group coordinator 10.13.4.33:9092 (id: 2147483647 rack: null) is unavailable or invalid due to cause: session timed out without receiving a heartbeat response. isDisconnected: false. Rediscovery will be attempted.
2024-10-09T16:32:27.265+05:30  INFO 46474 --- [kafka-demo] [at-thread | foo] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-foo-1, groupId=foo] Requesting disconnect from last known coordinator 10.13.4.33:9092 (id: 2147483647 rack: null)
2024-10-09T16:32:27.266+05:30  INFO 46474 --- [kafka-demo] [at-thread | foo] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-foo-1, groupId=foo] Client requested disconnect from node 2147483647
2024-10-09T16:32:27.796+05:30  INFO 46474 --- [kafka-demo] [ntainer#0-0-C-1] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-foo-1, groupId=foo] Discovered group coordinator 10.13.4.33:9092 (id: 2147483647 rack: null)
Consumed message: hellp
customerJson: as string: {"customerId":11,"firstName":"Revi","lastName":"Hyderabad","email":"revi.e@hotmail.com"}
Consumed message: {"customerId":11,"firstName":"Revi","lastName":"Hyderabad","email":"revi.e@hotmail.com"}
customerJson: as string: {"customerId":11,"firstName":"Revi","lastName":"Hyderabad","email":"revi.e@hotmail.com"}
Consumed message: {"customerId":11,"firstName":"Revi","lastName":"Hyderabad","email":"revi.e@hotmail.com"}
customerJson: as string: {"customerId":11,"firstName":"Revi","lastName":"Hyderabad","email":"revi.e@hotmail.com"}
Consumed message: {"customerId":11,"firstName":"Revi","lastName":"Hyderabad","email":"revi.e@hotmail.com"}

```


## gRPC Demo



## Netflix Conductor Demo



## Whole Microservices with JWT Authentication Enabled

- Make sure all services are up and running

- Postman collection given `Microservices.postman_collection.json` on api-gateway repo, kindly download 

- High level changes

```
1. API Gateway  - Added AuthenticationFilter which validates whether all routes are adding with Header `Authorization` and ignored few routes for inital register/get_token 

2. Added filters:
            - AuthenticationFilter on application.yml for existing services Product/Orders

3. AuthService: Added Eureka Client Enablement and added register endpoint  
  

```

- You can try running following APIs in order to see how this demo working with API Gateway as single point of contact

    1. Auth-Register-User
    2. Auth-Genarate-Token
    3. Auth-with-JWT
    4. Auth-Public-API
    5. POST Customers-LB  then GET
    6. POST Products-LB  then GET
    7. Products-LB-MissingHeader and Products-LB-InvalidHeader - to see if we miss auth header 
    8. POST Orders-LB and GET


