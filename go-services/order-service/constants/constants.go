package constants

const (
	CustomerServiceURL = "http://customer-service:1000/api/customers"
	ProductServiceURL  = "http://product-service:2000/api/products"
	GenerateTokenURL   = "http://customer-service:1000/api/token"
)

type TokenResponse struct {
	Token string `json:"token"`
	Exp   int64  `json:"exp"`
}
