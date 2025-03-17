package middleware

import (
	"net/http"

	"github.com/gorilla/handlers"
	"github.com/sirupsen/logrus"
)

func EnableCORS(handler http.Handler) http.Handler {
	logrus.Info("CORS Enabled")
	return handlers.CORS(
		handlers.AllowedOrigins([]string{"http://localhost:4200"}), // ✅ Allow Angular frontend
		handlers.AllowedMethods([]string{"GET", "POST", "OPTIONS"}),
		handlers.AllowedHeaders([]string{"Content-Type", "Authorization"}),
	)(handler)
}
