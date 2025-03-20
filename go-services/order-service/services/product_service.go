package services

import (
	"fmt"
	"order-service/constants"
	"order-service/utils"

	"github.com/gin-gonic/gin"
)

func FetchProduct(c *gin.Context, productID uint) (map[string]interface{}, error) {
	url := constants.ProductServiceURL + "/" + fmt.Sprint(productID)
	token, err := FetchToken(c)
	if err != nil {
		return nil, err
	}
	return utils.MakeGETRequest(url, token)
}
