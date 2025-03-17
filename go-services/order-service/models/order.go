package models

import "time"

type Order struct {
	ID         uint      `json:"id" gorm:"primaryKey"`
	CustomerID uint      `json:"customer_id"`
	ProductID  uint      `json:"product_id"`
	Quantity   int       `json:"quantity"`
	PerCost    float64   `json:"per_cost"`
	TotalCost  float64   `json:"total_cost"`
	CreatedAt  time.Time `json:"created_at"`
	Status     string    `json:"status"`
}
