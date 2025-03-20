package kafka

import (
	"log"

	"github.com/confluentinc/confluent-kafka-go/kafka"
)

// KafkaProducer sends order events to Kafka
func KafkaProducer(topic string, message []byte) error {
	producer, err := kafka.NewProducer(&kafka.ConfigMap{
		"bootstrap.servers": "localhost:9092",
	})
	if err != nil {
		return err
	}
	defer producer.Close()

	// Produce the message to Kafka
	err = producer.Produce(&kafka.Message{
		TopicPartition: kafka.TopicPartition{Topic: &topic, Partition: kafka.PartitionAny},
		Value:          message,
	}, nil)

	if err != nil {
		log.Println("❌ Failed to produce message:", err)
		return err
	}

	log.Println("✅ Order event published to Kafka:", string(message))
	return nil
}
