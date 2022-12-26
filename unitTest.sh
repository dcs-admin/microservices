# New comment
for i in `seq 1 2`; do  
    
 

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

 

 curl --location --request POST 'http://localhost:9002/customers/' \
 --header 'Content-Type: application/json' \
--data-raw '{
    "firstName": "Ramu",
    "lastName": "Evana",
    "email": "ramu.evana@gmail.com"
}'


curl --location --request GET 'http://localhost:9002/customers/1'

 

 curl --location --request POST 'http://localhost:9005/orders/' \
 --header 'Content-Type: application/json' \
--data-raw '{
    "customerId": "1",
    "productId": "1",
    "location": "Hyderabad"
}'


curl --location --request GET 'http://localhost:9005/orders/1'



    echo "\n        SUCCESS"
done
