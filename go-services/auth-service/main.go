package main

import (
	"auth-service/database"
	"auth-service/handlers"
	"auth-service/middleware"
	"log"
	"net/http"

	"github.com/gorilla/mux"
)

func main() {
	database.InitDB()
	router := mux.NewRouter()

	//router.Use(middleware.LoggingMiddleware)
	//router.Use(middleware.EnableCORS)
	middleware.LoggingMiddleware(router)

	router.HandleFunc("/api/auth/register", handlers.RegisterHandler).Methods("POST")
	router.HandleFunc("/api/auth/login", handlers.LoginHandler).Methods("POST")

	// 🌍 Apply CORS middleware

	log.Println("Auth service running on port 8888")
	log.Fatal(http.ListenAndServe(":8888", middleware.EnableCORS(router)))
}
