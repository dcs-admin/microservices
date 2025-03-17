package middleware

import (
	"github.com/gin-gonic/gin"
	"github.com/sirupsen/logrus"
)

func EnableCORS() gin.HandlerFunc {
	// logrus.Info("CORS Enabled")
	// return handlers.CORS(
	// 	handlers.AllowedOrigins([]string{"http://localhost:4200"}), // ✅ Allow Angular frontend
	// 	handlers.AllowedMethods([]string{"GET", "POST", "OPTIONS"}),
	// 	handlers.AllowedHeaders([]string{"Content-Type", "Authorization"}),
	// )(handler)

	logrus.Info("NEW CORS Enabled")
	return func(c *gin.Context) {
		c.Writer.Header().Set("Access-Control-Allow-Origin", "http://localhost:4200")
		c.Writer.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		c.Writer.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		// Handle preflight requests
		if c.Request.Method == "OPTIONS" {
			c.AbortWithStatus(204) // No Content
			return
		}

		c.Next()
	}
}
