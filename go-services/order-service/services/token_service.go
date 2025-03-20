package services

import (
	"log"
	"order-service/constants"
	"order-service/utils"
	"sync"
	"time"

	"github.com/gin-gonic/gin"
)

// Global token cache
var (
	tokenCache     string
	tokenExpiry    time.Time
	tokenCacheLock sync.Mutex
)

//	type TokenResponse struct {
//		Token string `json:"token"`
//		Exp   int64  `json:"exp"`
//	}
func FetchToken(c *gin.Context) (string, error) {
	tokenCacheLock.Lock()
	defer tokenCacheLock.Unlock()

	// 🛑 Check if a valid token is already cached
	if time.Now().Before(tokenExpiry) && tokenCache != "" {
		log.Println("✅ Using Cached Token (Valid Until:", tokenExpiry, ")")
		return tokenCache, nil
	}

	log.Println("🔄 Fetching New Token...")

	tokenResp, err := utils.RequestToken(constants.GenerateTokenURL, c)
	if err != nil {
		log.Println("🚨 Error fetching token:", err)
		return "", err
	}

	// 🛑 Ensure Expiration Time is set
	if tokenResp.Exp == 0 {
		log.Println("🚨 Token API did not return expiration time! Defaulting to 15 minutes.")
		tokenResp.Exp = time.Now().Add(15 * time.Minute).Unix()
	}

	// Cache the new token
	tokenCache = tokenResp.Token
	tokenExpiry = time.Unix(tokenResp.Exp, 0).Add(-5 * time.Minute) // Refresh 5 minutes before expiry

	log.Println("✅ New Token Cached (Valid Until:", tokenExpiry, ")")

	return tokenCache, nil
}
