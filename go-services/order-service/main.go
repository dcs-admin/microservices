package main

import (
	"log"
	"net/http"
	"order-service/config"
	"order-service/database"
	"order-service/middleware"
	"order-service/routes"
	"os"

	"github.com/gin-gonic/gin"
)

func main() {
	// config.LoadConfig()
	// database.InitDB()

	// router := routes.SetupRouter()
	// //r.Use(middleware.EnableCORS())

	// middleware.LoggingMiddleware(router)

	// middleware.JWTMiddleware(router)
	// //router.Use(middleware.JWTMiddleware())

	// port := os.Getenv("SERVER_PORT")
	// if port == "" {
	// 	port = "1002"
	// }

	// // log.Println("Orders Service running on port " + port)
	// // r.Run(":" + port)

	// log.Println("Orders service running on port 3000")
	// log.Fatal(http.ListenAndServe(":3000", middleware.EnableCORS(router)))

	// Load configuration and initialize database
	config.LoadConfig()
	database.InitDB()

	// Create Gin router
	router := gin.Default()

	// Enable CORS
	router.Use(middleware.EnableCORS())

	// Apply logging middleware
	//router.Use(middleware.LoggingMiddleware())

	// Apply JWT authentication middleware to all routes (or specific ones)
	router.Use(middleware.JWTMiddleware())

	// Set up routes
	routes.SetupRouter(router)

	// Determine port
	port := os.Getenv("SERVER_PORT")
	if port == "" {
		port = "3000"
	}

	log.Println("Orders service running on port " + port)
	log.Fatal(http.ListenAndServe(":"+port, router))

}
