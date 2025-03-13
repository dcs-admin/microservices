
## Go Services Practise

Key concepts covered in my article 

- Web Security and Attacks
-  Web Attacks
-  CSRF - protected - /submit
-  XSS - http://localhost:1234/xss?name=%3Cscript%3E%20alert(%27Anji%27)%3C/script%3E
-  CORS - Simple Request cors with allowed origins and http methods
-  JWT Token Authorization
-  Rate Limiting

### Customer Service

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

### Product Service


### Order Service