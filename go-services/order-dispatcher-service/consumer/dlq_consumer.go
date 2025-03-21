package consumer

import (
	"log"
	"os"

	"github.com/confluentinc/confluent-kafka-go/kafka"
)

// KafkaDLQConsumer listens for failed messages in DLQ
func KafkaDLQConsumer() {
	kafkaBrokerUrl := os.Getenv("KAFKA_BROKER_URL")
	dlqTopic := os.Getenv("KAFKA_DLQ_TOPIC")
	groupID := os.Getenv("KAFKA_GROUP")

	consumer, err := kafka.NewConsumer(&kafka.ConfigMap{
		"bootstrap.servers": kafkaBrokerUrl,
		"group.id":          groupID,
		"auto.offset.reset": "earliest",
	})
	if err != nil {
		log.Fatalf("🚨 Failed to create DLQ consumer: %v", err)
	}
	defer consumer.Close()

	err = consumer.SubscribeTopics([]string{dlqTopic}, nil)
	if err != nil {
		log.Fatalf("🚨 Failed to subscribe to DLQ topic: %v", err)
	}

	log.Println("☠️ Dead Letter Queue Consumer started...")
	for {
		msg, err := consumer.ReadMessage(-1)
		if err == nil {
			log.Printf("💀 Received from DLQ: %s\n", string(msg.Value))
			// Handle failed messages (e.g., alert, retry, log)
		} else {
			log.Printf("⚠️ Error reading from DLQ: %v\n", err)
		}
	}
}
