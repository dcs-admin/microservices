Install Kafka
https://hevodata.com/learn/install-kafka-on-mac/ ==> https://kafka.apache.org/downloads

tar -xzf kafka_2.13-3.0.0.tgz 

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



Springbot App for Demo
