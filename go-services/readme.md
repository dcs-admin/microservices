
# Go Services Practise
This proect represents an **architecture diagram** for a **Go microservices-based e-commerce system**. Here’s a breakdown of the components and their interactions:

---

### 🔹 **Frontend (Client)**
- **Goui:4200** (Angular 16, Node 18)
  - The frontend UI built using **Angular**.
  - Interacts with the backend via **api-gateway**.

  ### GoUI

Angular application

```

cd goui

vevana@FWS-CHE-LT-7895 goui % node --version
v18.12.1


vevana@FWS-CHE-LT-7895 goui % ng version

     _                      _                 ____ _     ___
    / \   _ __   __ _ _   _| | __ _ _ __     / ___| |   |_ _|
   / △ \ | '_ \ / _` | | | | |/ _` | '__|   | |   | |    | |
  / ___ \| | | | (_| | |_| | | (_| | |      | |___| |___ | |
 /_/   \_\_| |_|\__, |\__,_|_|\__,_|_|       \____|_____|___|
                |___/
    

Angular CLI: 16.0.6
Node: 18.12.1
Package Manager: npm 9.7.1
OS: darwin x64

Angular: 16.2.12
... animations, common, compiler, compiler-cli, core, forms
... platform-browser, platform-browser-dynamic, router

Package                         Version
---------------------------------------------------------
@angular-devkit/architect       0.1602.16
@angular-devkit/build-angular   16.2.16
@angular-devkit/core            16.2.16
@angular-devkit/schematics      16.0.6
@angular/cli                    16.0.6
@schematics/angular             16.0.6
rxjs                            7.8.2
typescript                      5.0.4
    
--

ng serve -o

```


---

### 🔹 **API Gateway**
- **api-gateway:1111** (Spring Boot 3.0)
  - Acts as a central entry point for client requests.
  - Handles:
    - **JWT Authentication** (Auth Middleware)
    - **CORS Policy**
    - **Rate Limiting**
    - **Security**
    - **CRUD Operations**
    - **Logging & Exception Handling**
  - Routes requests to backend microservices.

---

### 🔹 **Backend Microservices (Go 1.23)**
Each service is written in **Go** and manages a specific domain.

#### **1️⃣ Authentication Service (`auth:8888`)**
- Handles **user authentication**.
- Issues **JWT tokens** for secure API calls.

#### **2️⃣ Customer Service (`customers:1000`)**
- Manages **customer data** stored in a database.
- Serves customer-related information.


```
cd /Users/vevana/anji/microservices/go-services/customer-service

go run main.go
```

- Customer CURD


```
curl --location --request GET 'http://localhost:8080/api/token' \
--header 'Content-Type: application/json' \
--header 'Cookie: _gorilla_csrf=MTc0MTg1MTkwNXxJamM1WlN0S2NuZzNTbTk1TlV4eFoybENUVk5OYzNGUGRXODBPSGRKYURWM1NUQXlabFp3VG5KTFNVazlJZ289fHESfvvaFUmnafk1v6M_Kd4dHqmzUmFj8P8SSpL1Gb_u' \
--data-raw '{
     
    "name": "Anji",
    "email":"anji@g.com",
    "address":"Hyd"
}'
```

```
curl --location 'http://localhost:8080/api/customers' \
--header 'Authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InRlc3R1c2VyIiwiZXhwIjoxNzQxOTU2OTgyfQ.24pfR52ZB0n-cJbTySw_Lz5b1gKa1FQzBRroLznE2qE' \
--header 'Content-Type: application/json' \
--header 'Cookie: _gorilla_csrf=MTc0MTg1MTkwNXxJamM1WlN0S2NuZzNTbTk1TlV4eFoybENUVk5OYzNGUGRXODBPSGRKYURWM1NUQXlabFp3VG5KTFNVazlJZ289fHESfvvaFUmnafk1v6M_Kd4dHqmzUmFj8P8SSpL1Gb_u' \
--data-raw '{
    "name": "Prabas Katikala",
    "email":"praba.sevana@g.com",
    "address":"Chennai"
}'
```


```
curl --location --request PUT 'http://localhost:8080/api/customers/3' \
--header 'Authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InRlc3R1c2VyIiwiZXhwIjoxNzQxOTU3NjA0fQ.3cWfV-IIuOaxa-euguZ1MqKJ42UQHv6aSnLbMn5K3Wk' \
--header 'Content-Type: application/json' \
--header 'Cookie: _gorilla_csrf=MTc0MTg1MTkwNXxJamM1WlN0S2NuZzNTbTk1TlV4eFoybENUVk5OYzNGUGRXODBPSGRKYURWM1NUQXlabFp3VG5KTFNVazlJZ289fHESfvvaFUmnafk1v6M_Kd4dHqmzUmFj8P8SSpL1Gb_u' \
--data '{
    "name": "Jagadamba"
     
}'
```

#### **3️⃣ Product Service (`products:2000`)**
- Stores and retrieves **product details**.
- Provides product information to **orders service** and UI.

#### **4️⃣ Order Service (`orders:3000`)**
- Handles **order placement and retrieval**.
- Stores orders in a database.
- **Kafka Producer**: Publishes events when a new order is created.

---

### 🔹 **Event-Driven Processing**
#### **Kafka (2.13-3) & Zookeeper**
- Kafka acts as a **message broker** for event-driven communication.
- **Order Service** publishes **order creation events** to Kafka.
- **Order Dispatcher Service** listens for events and processes them.

---

### 🔹 **Order Dispatcher Service**
- **Listens to Kafka topics** for new order events.
- Updates order status (e.g., `Dispatched`).
- Notifies external systems (e.g., warehouses, shipping services).

---

### 🔹 **Service Discovery**
- **service-registry:9999**
  - A **service registry** (like Eureka, Consul, or etcd).
  - Allows dynamic service discovery for backend services.

---

### 🔹 **Database Layer**
Each microservice has its own **database** for **data isolation**:
- **Customers DB** → Stores customer details.
- **Products DB** → Stores product data.
- **Orders DB** → Stores order records.

---

### 🔥 **Summary of Workflow**
1. **Customer places an order** via **Goui (Angular UI)**.
2. **API Gateway routes the request** to the **Order Service**.
3. **Order Service stores the order** in the **database**.
4. **Order Service publishes an event** to Kafka.
5. **Order Dispatcher Service listens** for the event and processes it.
6. **Order status is updated** to `"Dispatched"` in the database.
7. **Customer gets real-time updates** on their order status.

