package handlers

import (
	"net/http"
	"order-service/database"
	"order-service/models"
	"strconv"
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

	// Add timestamp to each order
	for i := range orders {
		orders[i].CreatedAt = time.Now()
	}

	// Save orders to database
	if err := database.DB.Create(&orders).Error; err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to create order"})
		return
	}

	// Send JSON response
	c.JSON(http.StatusCreated, gin.H{"message": "Order placed successfully"})
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

	logrus.Info("GetOrders request received", c.Request.Context().Value(userIDKey))

	// Retrieve user ID from request context
	userID, ok := c.Request.Context().Value(userIDKey).(int)
	if !ok {
		c.JSON(http.StatusUnauthorized, gin.H{"error": "Unauthorized - User ID not found in token"})
		return
	}

	// Convert user ID to string for querying DB
	customerIDStr := strconv.Itoa(userID)

	// Check for optional order_id query parameter
	orderID := c.Query("order_id")

	// Prepare query
	var orders []models.Order
	query := database.DB.Where("customer_id = ?", customerIDStr)

	if orderID != "" {
		query = query.Where("id = ?", orderID)
	}

	// Execute query
	query.Find(&orders)
	c.JSON(http.StatusOK, orders)
}
