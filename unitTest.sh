
for i in `seq 1 2`; do  
    echo "Save Department"
    curl --location --request POST 'http://localhost:9191/departments/' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "departmentName": "CSE",
        "departmentAddress": "Hyderabad",
        "departmentCode": "CSE_01"
    }'
    echo "\n        SUCCESS"


    echo "GET Department"
    curl --location --request GET 'http://localhost:9191/departments/'${i}


    echo "Save User " 
    curl --location --request POST 'http://localhost:9191/users/' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "firstName": "Ramu",
        "lastName": "Evana",
        "email": "ramu.evana@gmail.com",
        "departmentId": "${i}"
    }'

    echo "\n       SUCCESS"


    echo "GET USER with calling of other service Department"
    curl --location --request GET 'http://localhost:9191/users/'${i}

    echo "\n        SUCCESS"
done
