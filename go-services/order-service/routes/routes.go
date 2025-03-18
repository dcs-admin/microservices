package routes

import (
	"order-service/controller"

	"github.com/gin-gonic/gin"
)

// SetupRouter initializes API routes
// func SetupRouter() *gin.Engine {
// 	r := gin.Default()

// 	r.POST("/api/orders", handlers.CreateOrderHandler)
// 	r.GET("/api/orders", handlers.GetOrders)

// 	return r
// }

// SetupRouter sets up routes for the orders service
func SetupRouter(router *gin.Engine) {
	api := router.Group("/api")
	{
		api.POST("/orders", controller.CreateOrderHandler)
		api.GET("/orders", controller.GetOrders)
		//api.GET("/orders/:id", controllers.GetOrderById)
	}
}
