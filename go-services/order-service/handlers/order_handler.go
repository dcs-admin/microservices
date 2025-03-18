package handlers

import (
	"net/http"
	"order-service/database"
	"order-service/models"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/sirupsen/logrus"
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

// GetOrders fetches orders based on customer_id or order_id
func GetOrders(c *gin.Context) {

	// Get user ID from context
	userID, exists := c.Get("user_id")
	logrus.Info("JWT: userID:", userID)
	if !exists {
		c.JSON(http.StatusUnauthorized, gin.H{"error": "User ID not found in context"})
		return
	}

	// Convert user ID to string for querying DB
	// customerIDStr := strconv.Itoa(userID)

	// Check for optional order_id query parameter
	orderID := c.Query("order_id")

	// Prepare query
	var orders []models.Order
	query := database.DB.Where("customer_id = ?", userID)

	if orderID != "" {
		query = query.Where("id = ?", orderID)
	}

	// Execute query
	query.Find(&orders)
	c.JSON(http.StatusOK, orders)
}
