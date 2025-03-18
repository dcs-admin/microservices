package controller

import (
	"net/http"
	"order-service/database"
	"order-service/models"
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

		product, err := fetchProduct(order.ProductID)
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

// fetchProduct fetches product details from products-service
func fetchProduct(productID uint) (map[string]interface{}, error) {
	url := fmt.Sprintf("%s/%d", productServiceURL, productID)
	log.Println("Fetching customer from:", url)

	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return nil, err
	}

	req.Header.Set("Authorization", string("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InRlc3R1c2VyIiwiZXhwIjoxNzQyMzk2MjM4fQ.VMZarQUj_5jxoIjPnWIeE2IEmJzrzBTTtoALkH_DAX0"))

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	// Check for non-200 status
	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("product service returned %d", resp.StatusCode)
	}

	var product map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&product); err != nil {
		return nil, err
	}

	return product, nil
}
