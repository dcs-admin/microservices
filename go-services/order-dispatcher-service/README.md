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

