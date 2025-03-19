package controller

import (
	"bytes"
	"io"
	"net/http"
	"order-service/database"
	"order-service/models"
	"sync"
	"time"

	"encoding/json"
	"fmt"
	"log"

	"github.com/gin-gonic/gin"
)

// Context key to retrieve user ID from middleware
type contextKey string

const userIDKey contextKey = "user_id"

// CreateOrderHandler handles order creation using Gin
func CreateOrderHandler(c *gin.Context) {
	var orders []models.Order

	// Bind JSON request to orders struct
	if err := c.ShouldBindJSON(&orders); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid request payload"})
		return
	}

	// Add timestamp and prepare order_ids slice
	var orderIDs []uint
	for i := range orders {
		orders[i].CreatedAt = time.Now()
	}

	// Save orders to the database
	if err := database.DB.Create(&orders).Error; err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to create orders"})
		return
	}

	// Collect all order IDs
	for _, order := range orders {
		orderIDs = append(orderIDs, order.ID)
	}

	// Send JSON response with order reference IDs
	c.JSON(http.StatusCreated, gin.H{
		"message":     "Orders placed successfully",
		"order_ids":   orderIDs,
		"order_count": len(orderIDs),
	})
}

// CreateOrder handles order creation
func CreateSingleOrder(c *gin.Context) {
	var order models.Order

	if err := c.ShouldBindJSON(&order); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid input"})
		return
	}

	order.TotalCost = float64(order.Quantity) * order.PerCost
	database.DB.Create(&order)

	c.JSON(http.StatusCreated, gin.H{"message": "Order created successfully", "order": order})
}

// Depricated :: GetOrders fetches orders based on customer_id or order_id
// Finds only from current service - not calling downstream services
// func GetOrders(c *gin.Context) {

// 	// Get user ID from context
// 	userID, exists := c.Get("user_id")
// 	logrus.Info("JWT: userID:", userID)
// 	if !exists {
// 		c.JSON(http.StatusUnauthorized, gin.H{"error": "User ID not found in context"})
// 		return
// 	}

// 	// Convert user ID to string for querying DB
// 	// customerIDStr := strconv.Itoa(userID)

// 	// Check for optional order_id query parameter
// 	orderID := c.Query("order_id")

// 	// Prepare query
// 	var orders []models.Order
// 	query := database.DB.Where("customer_id = ?", userID)

// 	if orderID != "" {
// 		query = query.Where("id = ?", orderID)
// 	}

// 	// Execute query
// 	query.Find(&orders)
// 	c.JSON(http.StatusOK, orders)
// }

// Service URLs
var customerServiceURL = "http://localhost:1000/api/customers"
var productServiceURL = "http://localhost:2000/api/products"
var GENERATE_TOKEN_URL string = "http://localhost:1000/api/token"

// GetOrders fetches orders along with customer and product details
func GetOrders(c *gin.Context) {
	var orders []models.Order
	//customerID := c.Query("customer_id")
	orderID := c.Query("order_id")
	userID, exists := c.Get("user_id")
	if !exists {
		c.JSON(http.StatusUnauthorized, gin.H{"error": "User ID not found in context"})
		return
	}

	query := database.DB.Where("customer_id = ?", userID)

	if orderID != "" {
		query = query.Where("id = ?", orderID)
	}

	if err := query.Find(&orders).Error; err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to fetch orders"})
		return
	}

	// Fetch customer and product details for each order
	enhancedOrders := []map[string]interface{}{}

	for _, order := range orders {
		customer, err := fetchCustomer(order.CustomerID)
		if err != nil {
			log.Println("Error fetching customer:", err)
			continue
		}

		product, err := fetchProduct(c, order.ProductID)
		if err != nil {
			log.Println("Error fetching product:", err)
			continue
		}

		// Build enhanced order response
		enhancedOrder := map[string]interface{}{
			"id":         order.ID,
			"quantity":   order.Quantity,
			"per_cost":   order.PerCost,
			"total_cost": order.TotalCost,
			"created_at": order.CreatedAt,
			"status":     order.Status,
			"customer":   customer,
			"product":    product,
		}
		enhancedOrders = append(enhancedOrders, enhancedOrder)
	}

	// Send JSON response
	c.JSON(http.StatusOK, enhancedOrders)
}

// fetchCustomer fetches customer details from customer-service
func fetchCustomer(customerID uint) (map[string]interface{}, error) {
	url := fmt.Sprintf("%s/%d", customerServiceURL, customerID)
	log.Println("Fetching customer from:", url)

	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return nil, err
	}

	//var token = middleware.GetJwtToken()
	// Add Authorization header
	req.Header.Set("Authorization", string("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InRlc3R1c2VyIiwiZXhwIjoxNzQyMzk2MjM4fQ.VMZarQUj_5jxoIjPnWIeE2IEmJzrzBTTtoALkH_DAX0"))

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	// Check for non-200 status
	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("customer service returned %d", resp.StatusCode)
	}

	var customer map[string]interface{}
	log.Println("My customer:", customer)
	if err := json.NewDecoder(resp.Body).Decode(&customer); err != nil {
		return nil, fmt.Errorf("invalid JSON response from customer service")
	}
	log.Println("My customer Now:", customer)

	return customer, nil
}

// // fetchProduct fetches product details from products-service
// func fetchProduct(productID uint) (map[string]interface{}, error) {
// 	url := fmt.Sprintf("%s/%d", productServiceURL, productID)
// 	log.Println("Fetching product from:", url)

// 	req, err := http.NewRequest("GET", url, nil)
// 	if err != nil {
// 		return nil, err
// 	}

// 	req.Header.Set("Authorization", string("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InRlc3R1c2VyIiwiZXhwIjoxNzQyMzk2MjM4fQ.VMZarQUj_5jxoIjPnWIeE2IEmJzrzBTTtoALkH_DAX0"))

// 	client := &http.Client{}
// 	resp, err := client.Do(req)
// 	if err != nil {
// 		return nil, err
// 	}
// 	defer resp.Body.Close()

// 	// Check for non-200 status
// 	if resp.StatusCode != http.StatusOK {
// 		return nil, fmt.Errorf("product service returned %d", resp.StatusCode)
// 	}

// 	var product map[string]interface{}
// 	if err := json.NewDecoder(resp.Body).Decode(&product); err != nil {
// 		return nil, err
// 	}

// 	return product, nil
// }

// Global token cache
var (
	tokenCache     string
	tokenExpiry    time.Time
	tokenCacheLock sync.Mutex
)

// TokenResponse represents the response from the token API
type TokenResponse struct {
	Token string `json:"token"`
	Exp   int64  `json:"exp"`
}

// fetchToken fetches a new token from the token API and caches it
func fetchToken(id int, email string) (string, error) {
	tokenCacheLock.Lock()
	defer tokenCacheLock.Unlock()

	// 🛑 Check if a valid token is already cached
	if time.Now().Before(tokenExpiry) && tokenCache != "" {
		log.Println("✅ Using Cached Token (Valid Until:", tokenExpiry, ")")
		return tokenCache, nil
	}

	log.Println("🔄 Fetching New Token...")

	// Prepare JSON request body
	reqBody := fmt.Sprintf(`{
		"id": %d,
		"email": "%s",
		"user_data": {
			"role": "admin",
			"age": 30
		}
	}`, id, email)

	// Convert string to []byte
	requestBody := []byte(reqBody)

	// Make the request
	req, err := http.NewRequest("POST", GENERATE_TOKEN_URL, bytes.NewBuffer(requestBody))
	if err != nil {
		return "", err
	}
	req.Header.Set("Content-Type", "application/json")

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	// Read response body
	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}

	// Check for API errors
	if resp.StatusCode != http.StatusOK {
		log.Println("🚨 Token API Error:", string(body))
		return "", fmt.Errorf("token service returned %d: %s", resp.StatusCode, string(body))
	}

	// Parse JSON response
	var tokenResp TokenResponse
	if err := json.Unmarshal(body, &tokenResp); err != nil {
		return "", err
	}

	// 🛑 Fix: Use Expiration Time from Response
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

// fetchProduct fetches product details from products-service
func fetchProduct(c *gin.Context, productID uint) (map[string]interface{}, error) {
	// Construct the product service URL
	url := fmt.Sprintf("%s/%d", productServiceURL, productID)
	log.Println("Fetching product from:", url)

	// Extract user details from context
	userID, email, err := getUserDetailsFromContext(c)
	if err != nil {
		log.Println("User details extraction failed:", err)
		c.JSON(http.StatusUnauthorized, gin.H{"error": err.Error()})
		return nil, nil
	}

	// Get the token (fetches new one if expired)
	token, err := fetchToken(userID, email)
	if err != nil {
		log.Println("Failed to fetch token:", err)
		return nil, fmt.Errorf("failed to fetch token: %v", err)
	}

	// Debug: Print the token
	log.Println("Using Token:", token)

	// Create a GET request
	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		log.Println("Error creating request:", err)
		return nil, err
	}

	// Attach Bearer token
	req.Header.Set("Authorization", "Bearer "+token)

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		log.Println("Error sending request:", err)
		return nil, err
	}
	defer resp.Body.Close()

	// Debug: Print response status
	log.Println("Response Status:", resp.StatusCode)

	// If not 200, print the response body for debugging
	if resp.StatusCode != http.StatusOK {
		bodyBytes, _ := io.ReadAll(resp.Body)
		log.Println("Response Body:", string(bodyBytes)) // Debugging the response content
		return nil, fmt.Errorf("product service returned %d", resp.StatusCode)
	}

	// Decode response
	var product map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&product); err != nil {
		log.Println("Error decoding response:", err)
		return nil, err
	}

	return product, nil
}

// getUserDetailsFromContext extracts userID and email from context
func getUserDetailsFromContext(c *gin.Context) (int, string, error) {
	userIDVal, exists := c.Get("user_id")
	if !exists {
		return 0, "", fmt.Errorf("user ID not found in context")
	}

	emailVal, exists := c.Get("email")
	if !exists {
		return 0, "", fmt.Errorf("email not found in context")
	}

	// Type assertion
	userID, ok := userIDVal.(int)
	if !ok {
		return 0, "", fmt.Errorf("invalid user ID format")
	}

	email, ok := emailVal.(string)
	if !ok {
		return 0, "", fmt.Errorf("invalid email format")
	}

	return userID, email, nil
}
