# order-dispatcher-service

Which works on order dispatching and updating database with `Dispatch` state by consuming/liseneing events from Producer(Orders Service)


# How to work with Kafka

Install Kafka
https://hevodata.com/learn/install-kafka-on-mac/ ==> https://kafka.apache.org/downloads

tar -xzf kafka_2.13-3.0.0.tgz 

cd /Users/vevana/kafka/kafka_2.13-3.7.0

>> Two servers need to be started here to make kafka and zookeper working condition with v2, After v3 no zookeper required

- Start Zookeeper:
./bin/zookeeper-server-start.sh config/zookeeper.properties


- Start Kafka server:
./bin/kafka-server-start.sh config/server.properties

Create a topic:
./bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic order-events

List Topics
./bin/kafka-topics.sh --list --bootstrap-server localhost:9092


Producer:
./bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic order-events

Consumer:
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic order-events --from-beginning



## How this service consuming the order details

```
INFO[0000] Auth Database  initiated!                    
2025/03/20 20:52:03 🚀 Order Dispatcher Service is running... Listening for events.
2025/03/20 20:52:03 📩 Received order event: {"created_at":"2025-03-20T20:51:16.409774+05:30","customer_id":6,"id":138,"product_id":4,"quantity":1,"status":"Pending","total_cost":9998}
2025/03/20 20:52:03 🚚 Dispatching Order #138 for Customer #6
2025/03/20 20:52:03 ✅ Order #138 updated to 'Dispatched' by Dispatched_service
2025/03/20 20:52:03 📩 Received order event: {"created_at":"2025-03-20T20:51:16.409775+05:30","customer_id":6,"id":139,"product_id":9,"quantity":1,"status":"Pending","total_cost":15999}
2025/03/20 20:52:03 🚚 Dispatching Order #139 for Customer #6
2025/03/20 20:52:03 ✅ Order #139 updated to 'Dispatched' by Dispatched_service
2025/03/20 20:52:03 📩 Received order event: {"created_at":"2025-03-20T20:51:16.409775+05:30","customer_id":6,"id":140,"product_id":8,"quantity":1,"status":"Pending","total_cost":699}
2025/03/20 20:52:03 🚚 Dispatching Order #140 for Customer #6
2025/03/20 20:52:03 ✅ Order #140 updated to 'Dispatched' by Dispatched_service
```