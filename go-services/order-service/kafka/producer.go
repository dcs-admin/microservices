package kafka

import (
	"log"
	"os"
	"time"

	"github.com/confluentinc/confluent-kafka-go/kafka"
)

// KafkaProducer sends order events to Kafka with retries
func KafkaProducer(topic string, message []byte) error {
	kafkaBrokerUrl := os.Getenv("KAFKA_BROKER_URL")
	dlqTopic := os.Getenv("KAFKA_DLQ_TOPIC") // Dead Letter Queue topic

	config := &kafka.ConfigMap{
		"bootstrap.servers": kafkaBrokerUrl,
		//"bootstrap.servers":  "my-kafka:9092",
		"security.protocol": "SASL_PLAINTEXT",
		"sasl.mechanism":    "PLAIN",
		"sasl.username":     "user",
		"sasl.password":     "wwbWUSY7QY",
	}

	producer, err := kafka.NewProducer(config)
	if err != nil {
		return err
	}
	defer producer.Close()

	// Retry logic: Try sending up to 3 times
	maxRetries := 3
	for i := 0; i < maxRetries; i++ {
		err = producer.Produce(&kafka.Message{
			TopicPartition: kafka.TopicPartition{Topic: &topic, Partition: kafka.PartitionAny},
			Value:          message,
		}, nil)

		if err == nil {
			log.Println("✅ Order event published successfully:", string(message))
			producer.Flush(2_000) //2sec
			return nil
		}

		log.Printf("⚠️ Retry %d/%d: Failed to publish message: %v\n", i+1, maxRetries, err)
		time.Sleep(2 * time.Second) // Wait before retrying
	}

	// If all retries fail, send the message to the Dead Letter Queue (DLQ)
	log.Println("❌ Sending failed message to Dead Letter Queue (DLQ):", string(message))
	err = producer.Produce(&kafka.Message{
		TopicPartition: kafka.TopicPartition{Topic: &dlqTopic, Partition: kafka.PartitionAny},
		Value:          message,
	}, nil)

	if err != nil {
		log.Println("🚨 Failed to publish message to DLQ:", err)
		return err
	}

	log.Println("☠️ Message moved to DLQ successfully")
	return nil
}

// KafkaProducer sends order events to Kafka - Without DLQ(DeadLetterQ)
// func KafkaProducer(topic string, message []byte) error {
// 	kafkaBrokerUrl := os.Getenv("KAFKA_BROKER_URL")
// 	producer, err := kafka.NewProducer(&kafka.ConfigMap{
// 		"bootstrap.servers": kafkaBrokerUrl,
// 	})
// 	if err != nil {
// 		return err
// 	}
// 	defer producer.Close()

// 	// Produce the message to Kafka
// 	err = producer.Produce(&kafka.Message{
// 		TopicPartition: kafka.TopicPartition{Topic: &topic, Partition: kafka.PartitionAny},
// 		Value:          message,
// 	}, nil)

// 	if err != nil {
// 		log.Println("❌ Failed to produce message:", err)
// 		return err
// 	}

// 	log.Println("✅ Order event published to Kafka:", string(message))
// 	return nil
// }
