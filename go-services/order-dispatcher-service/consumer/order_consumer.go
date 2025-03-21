package consumer

import (
	"encoding/json"
	"log"
	"order-dispatcher-service/database"
	"order-dispatcher-service/models"
	"order-dispatcher-service/notifications"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/confluentinc/confluent-kafka-go/kafka"
)

func Consume_orders() {
	kafkaBrokerUrl := os.Getenv("KAFKA_BROKER_URL")
	// Kafka configuration
	config := &kafka.ConfigMap{
		"bootstrap.servers": kafkaBrokerUrl,
		"group.id":          "order-dispatcher-group",
		"auto.offset.reset": "earliest",
	}

	// Create Kafka Consumer
	consumer, err := kafka.NewConsumer(config)
	if err != nil {
		log.Fatalf("Failed to create Kafka consumer: %v", err)
	}
	defer consumer.Close()

	// Subscribe to topic
	topic := "order-events"
	err = consumer.SubscribeTopics([]string{topic}, nil)
	if err != nil {
		log.Fatalf("Failed to subscribe to Kafka topic: %v", err)
	}

	// Handle system signals for graceful shutdown
	sigchan := make(chan os.Signal, 1)
	signal.Notify(sigchan, syscall.SIGINT, syscall.SIGTERM)

	log.Println("🚀 Order Dispatcher Service is running... Listening for events.")

	for {
		select {
		case sig := <-sigchan:
			log.Printf("Received signal %v, shutting down...", sig)
			return

		default:
			// Read message from Kafka
			msg, err := consumer.ReadMessage(-1)
			if err == nil {
				log.Printf("📩 Received order event: %s\n", string(msg.Value))

				// Process Order Event
				var order models.Order
				if err := json.Unmarshal(msg.Value, &order); err != nil {
					log.Printf("⚠️ Error decoding order event: %v", err)
					continue
				}

				// Dispatch Order Processing Logic
				processOrder(order)
			} else {
				log.Printf("⚠️ Error consuming Kafka message: %v\n", err)
			}
		}
	}
}

// processOrder simulates processing the order
func processOrder(order models.Order) {
	log.Printf("🚚 Dispatching Order #%d for Customer #%d\n", order.ID, order.CustomerID)
	// Implement order processing logic here (e.g., notifying warehouse, updating status)

	// Update order status in the database
	result := database.DB.Model(&models.Order{}).
		Where("id = ?", order.ID).
		Updates(map[string]interface{}{
			"status":     "Dispatched",
			"updated_by": "order-dispatcher-service",
			"updated_at": time.Now(),
		})

	//TODO Send an Email/SMS/WhatsApp/Slack

	//Send Slack Notification
	notifications.SendSlackAlert(order, "Order has consumed and updated the status `Pending` to `Dispatched` ")

	if result.Error != nil {
		log.Printf("❌ Failed to update order #%d: %v\n", order.ID, result.Error)
	} else {
		log.Printf("✅ Order #%d updated to 'Dispatched' by Dispatched_service\n", order.ID)
	}

}
