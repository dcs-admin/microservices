
cd service-registry && mvn spring-boot:run 

cd api-gateway && mvn spring-boot:run 

cd cloud-config-server && mvn spring-boot:run 

cd product-service && mvn spring-boot:run 

cd customer-service && mvn spring-boot:run 

cd order-service && mvn spring-boot:run 



cd hystrix-dashboard && mvn spring-boot:run 

## Open Following now
# SERVICE-REGISTRY   - http://localhost:8761
# 
# API-GATEWAY	     - 192.168.1.1:API-GATEWAY:9191
# CONFIG-SERVER	     - 192.168.1.1:CONFIG-SERVER:9296 
# HYSTRIX-DASHBOARD	 - 192.168.1.1:HYSTRIX-DASHBOARD:9295
# 
# PRODUCT-SERVICE	  - 192.168.1.1:PRODUCT-SERVICE:9001
# CUSTOMER-SERVICE	 - 192.168.1.1:CUSTOMER-SERVICE:9002
# ORDER-SERVICE	    - 192.168.1.1:ORDER-SERVICE:9005