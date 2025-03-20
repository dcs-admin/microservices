package services

import (
	"log"
	"order-service/database"
	"order-service/models"

	"github.com/gin-gonic/gin"
)

func FetchOrdersDeprecated(c *gin.Context) ([]map[string]interface{}, error) {
	var orders []models.Order
	userID, exists := c.Get("user_id")
	if !exists {
		log.Println("ID not present in token/context")
		return nil, nil
	}

	query := database.DB.Where("customer_id = ?", userID)
	if err := query.Find(&orders).Error; err != nil {
		return nil, err
	}

	enhancedOrders := []map[string]interface{}{}
	for _, order := range orders {
		customer, err := FetchCustomer(c, order.CustomerID)
		if err != nil {
			log.Println("Error fetching customer:", err)
			continue
		}

		product, err := FetchProduct(c, order.ProductID)
		if err != nil {
			log.Println("Error fetching product:", err)
			continue
		}

		enhancedOrders = append(enhancedOrders, map[string]interface{}{
			"id":         order.ID,
			"quantity":   order.Quantity,
			"per_cost":   order.PerCost,
			"total_cost": order.TotalCost,
			"created_at": order.CreatedAt,
			"status":     order.Status,
			"customer":   customer,
			"product":    product,
		})
	}
	return enhancedOrders, nil
}

func FetchOrders(c *gin.Context) ([]map[string]interface{}, error) {
	var orders []models.Order
	userID, exists := c.Get("user_id")
	if !exists {
		log.Println("ID not present in token/context")
		return nil, nil
	}

	// Fetch orders from DB
	query := database.DB.Where("customer_id = ?", userID)
	if err := query.Find(&orders).Error; err != nil {
		return nil, err
	}

	// 🛑 Step 1: Get unique customer IDs and product IDs
	customerIDs := make(map[uint]bool)
	productIDs := make(map[uint]bool)

	for _, order := range orders {
		customerIDs[order.CustomerID] = true
		productIDs[order.ProductID] = true
	}

	// 🛑 Step 2: Fetch unique customers and products in bulk
	customerMap := make(map[uint]interface{})
	productMap := make(map[uint]interface{})

	for customerID := range customerIDs {
		customer, err := FetchCustomer(c, customerID)
		if err != nil {
			log.Println("Error fetching customer:", err)
			continue
		}
		customerMap[customerID] = customer
	}

	for productID := range productIDs {
		product, err := FetchProduct(c, productID)
		if err != nil {
			log.Println("Error fetching product:", err)
			continue
		}
		productMap[productID] = product
	}

	// 🛑 Step 3: Construct enhanced orders using cached data
	enhancedOrders := []map[string]interface{}{}
	for _, order := range orders {
		enhancedOrders = append(enhancedOrders, map[string]interface{}{
			"id":         order.ID,
			"quantity":   order.Quantity,
			"per_cost":   order.PerCost,
			"total_cost": order.TotalCost,
			"created_at": order.CreatedAt,
			"status":     order.Status,
			"customer":   customerMap[order.CustomerID],
			"product":    productMap[order.ProductID],
		})
	}

	return enhancedOrders, nil
}
