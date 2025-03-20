package services

import (
	"fmt"
	"log"
	"order-service/constants"
	"order-service/utils"

	"github.com/gin-gonic/gin"
)

func FetchCustomer(c *gin.Context, customerID uint) (map[string]interface{}, error) {
	url := constants.CustomerServiceURL + "/" + fmt.Sprint(customerID)
	token, err := FetchToken(c)
	if err != nil {
		log.Println("Error in customer-service: FetchCustomer: ", err)
		return nil, err
	}
	return utils.MakeGETRequest(url, token)
}
