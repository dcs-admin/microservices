package constants

const (
	CustomerServiceURL = "http://localhost:1000/api/customers"
	ProductServiceURL  = "http://localhost:2000/api/products"
	GenerateTokenURL   = "http://localhost:1000/api/token"
)

type TokenResponse struct {
	Token string `json:"token"`
	Exp   int64  `json:"exp"`
}
