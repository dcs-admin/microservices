package main

import (
	"order-dispatcher-service/config"
	"order-dispatcher-service/consumer"
	"order-dispatcher-service/database"
)

func main() {

	config.LoadConfig()
	database.InitDB()
	consumer.Consume_orders()
}
