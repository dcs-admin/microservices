package middleware

import (
	"log"
	"net/http"
	"os"
	"time"

	"github.com/sirupsen/logrus"
)

func LoggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		next.ServeHTTP(w, r)
		log.Printf("[%s] %s %s", r.Method, r.RequestURI, time.Since(start))

		// Create a log file
		file, err := os.OpenFile("auth-service.log", os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
		if err != nil {
			logrus.Fatal("Failed to open log file:", err)
		}

		// Set Logrus to write to the file
		logrus.SetOutput(file)
		logrus.SetFormatter(&logrus.JSONFormatter{}) // Optional: Format logs as JSON

		logrus.Info("Application started!")
	})
}
