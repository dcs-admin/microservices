package main

import (
	//"database/sql"
	//"fmt"
	//"strconv"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"time"

	"gorm.io/datatypes"

	"github.com/dgrijalva/jwt-go"
	"github.com/go-playground/validator/v10"
	_ "github.com/go-sql-driver/mysql"
	"github.com/gorilla/handlers"
	"github.com/gorilla/mux"
	"github.com/joho/godotenv"
	"github.com/sirupsen/logrus"
	"golang.org/x/time/rate"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

// var db *sql.DB
var db *gorm.DB
var validate *validator.Validate
var jwtKey = []byte("MTc0MTg1MTg0NXxJa2xVY1hOVmNrWnhTWGQ1U1VSdmRqWTNZa3BNYVRsWVdGbGlVemR3TWsxb1ltbENUa1U0YkZNMlZGVTlJZ289fE07KlEGjYRED5UbyhiM_l6vVI33sYzVhU_TpR54Uy7Q")
var limiter = rate.NewLimiter(1, 50)

// Product model
type Product struct {
	ID             uint           `json:"id"`
	Name           string         `json:"name"`
	Description    string         `json:"description"`
	Price          float64        `json:"price"`
	Currency       string         `json:"currency"`
	StockQuantity  int            `json:"stock_quantity"`
	Category       string         `json:"category"`
	Brand          string         `json:"brand"`
	SKU            string         `json:"sku"`
	Images         datatypes.JSON `json:"images" gorm:"type:json"`
	Ratings        float64        `json:"ratings"`
	Reviews        datatypes.JSON `json:"reviews" gorm:"type:json"`
	CreatedAt      time.Time      `json:"created_at"`
	UpdatedAt      time.Time      `json:"updated_at"`
	AdditionalInfo string         `json:"additional_info"`
	Specifications string         `json:"specifications"`
	Model          string         `json:"model"`
}

type Claims struct {
	Username string `json:"username"`
	jwt.StandardClaims
}

// Middleware for logging
func loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		logrus.WithFields(logrus.Fields{
			"method": r.Method,
			"path":   r.URL.Path,
		}).Info("Received request")
		next.ServeHTTP(w, r)
	})
}

// Middleware for rate limiting
func rateLimitMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if !limiter.Allow() {
			http.Error(w, "Too many requests", http.StatusTooManyRequests)
			return
		}
		next.ServeHTTP(w, r)
	})
}

// Generate JWT token
func generateToken(w http.ResponseWriter, r *http.Request) {
	claims := &Claims{
		Username: "testuser",
		StandardClaims: jwt.StandardClaims{
			ExpiresAt: time.Now().Add(24 * time.Hour).Unix(),
		},
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	tokenString, err := token.SignedString(jwtKey)
	if err != nil {
		http.Error(w, "Could not generate token", http.StatusInternalServerError)
		return
	}
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]string{"token": tokenString})
}

// Middleware for authentication
func authMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Skip authentication for the token endpoint
		if r.URL.Path == "/api/token" {
			next.ServeHTTP(w, r)
			return
		}

		tokenString := r.Header.Get("Authorization")
		if tokenString == "" {
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		claims := &Claims{}
		token, err := jwt.ParseWithClaims(tokenString, claims, func(token *jwt.Token) (interface{}, error) {
			return jwtKey, nil
		})
		if err != nil || !token.Valid {
			http.Error(w, "Invalid token", http.StatusUnauthorized)
			return
		}

		next.ServeHTTP(w, r)
	})
}

func initLogging() {
	// Create a log file
	file, err := os.OpenFile("app.log", os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
	if err != nil {
		logrus.Fatal("Failed to open log file:", err)
	}

	// Set Logrus to write to the file
	logrus.SetOutput(file)
	logrus.SetFormatter(&logrus.JSONFormatter{}) // Optional: Format logs as JSON

	logrus.Info("Application started!")
}

// Initialize DB Connection
func initDB() {
	// Load .env file
	err := godotenv.Load("config.env")
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	// Read database credentials from env
	dbUser := os.Getenv("DB_USER")
	dbPassword := os.Getenv("DB_PASSWORD")
	dbHost := os.Getenv("DB_HOST")
	dbPort := os.Getenv("DB_PORT")
	dbName := os.Getenv("DB_NAME")
	dbCharset := os.Getenv("DB_CHARSET")
	dbOptions := os.Getenv("DB_OPTIONS")

	// Construct MySQL connection string
	dsn := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=%s&%s",
		dbUser, dbPassword, dbHost, dbPort, dbName, dbCharset, dbOptions)

	fmt.Println("MySQL DSN:", dsn)
	logrus.Info("MySQL DSN:", dsn)
	//dsn := "root:p2wd1234@123@tcp(localhost:3306)/product?charset=utf8mb4&parseTime=True&loc=Local"
	//var err error
	db, err = gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Fatal("Failed to connect to database:", err)
	}

	// AutoMigrate the Product table
	db.AutoMigrate(&Product{})
	logrus.Info("Database  initiated!")
}

// Create a New Product (POST /products)
func CreateProduct(w http.ResponseWriter, r *http.Request) {
	var product Product
	if err := json.NewDecoder(r.Body).Decode(&product); err != nil {
		http.Error(w, "Invalid request payload", http.StatusBadRequest)
		return
	}

	if err := db.Create(&product).Error; err != nil {
		http.Error(w, "Error saving product", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(product)
	productJSON, _ := json.Marshal(product)
	logrus.Info("Product created: " + string(productJSON))
}

// Get All Products (GET /products)
func GetAllProducts(w http.ResponseWriter, r *http.Request) {
	var products []Product
	db.Find(&products)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(products)

	productJSON, _ := json.Marshal(products)
	logrus.Info("GetAllProducts: Products List " + string(productJSON))
}

// Get Product by ID (GET /products/{id})
func GetProductByID(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	var product Product
	if err := db.First(&product, params["id"]).Error; err != nil {
		http.Error(w, "Product not found", http.StatusNotFound)
		return
	}
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(product)

	productJSON, _ := json.Marshal(product)
	logrus.Info("GetProductByID:: " + string(productJSON))
}

// Update Product (PUT /products/{id})
func UpdateProduct(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	var product Product

	// Check if product exists
	if err := db.First(&product, params["id"]).Error; err != nil {
		http.Error(w, "Product not found", http.StatusNotFound)
		return
	}

	// Decode request body
	if err := json.NewDecoder(r.Body).Decode(&product); err != nil {
		http.Error(w, "Invalid request payload", http.StatusBadRequest)
		return
	}

	db.Save(&product)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(product)

	productJSON, _ := json.Marshal(product)
	logrus.Info("UpdateProduct:: " + string(productJSON))
}

// Delete Product (DELETE /products/{id})
func DeleteProduct(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	var product Product

	// Check if product exists
	if err := db.First(&product, params["id"]).Error; err != nil {
		http.Error(w, "Product not found", http.StatusNotFound)
		return
	}

	db.Delete(&product)
	w.WriteHeader(http.StatusNoContent)

	productJSON, _ := json.Marshal(product)
	logrus.Info("DeleteProduct:: " + string(productJSON))
}

// 🎯 Function to apply CORS middleware
func setupCORS(handler http.Handler) http.Handler {
	return handlers.CORS(
		handlers.AllowedOrigins([]string{"http://localhost:4200"}), // ✅ Allow Angular frontend
		handlers.AllowedMethods([]string{"GET", "POST", "OPTIONS"}),
		handlers.AllowedHeaders([]string{"Content-Type", "Authorization"}),
	)(handler)
}

// Handled Auth, CORS, RateLimiting middleware
// API Security
// CURD
// Logging
// Exception Handling
func main() {
	initLogging()
	initDB()

	validate = validator.New()
	r := mux.NewRouter()

	//r.Handle("/api/products", authMiddleware(rateLimitMiddleware(http.HandlerFunc(createProduct)))).Methods("POST")

	// Group routes under a subrouter to apply middleware globally
	apiRouter := r.PathPrefix("/api").Subrouter()
	// Define Routes
	apiRouter.HandleFunc("/token", generateToken).Methods("GET")
	apiRouter.HandleFunc("/products", CreateProduct).Methods("POST")
	apiRouter.HandleFunc("/products", GetAllProducts).Methods("GET")
	apiRouter.HandleFunc("/products/{id}", GetProductByID).Methods("GET")
	apiRouter.HandleFunc("/products/{id}", UpdateProduct).Methods("PUT")
	apiRouter.HandleFunc("/products/{id}", DeleteProduct).Methods("DELETE")

	// Apply Middlewares globally
	withMiddleware := authMiddleware(rateLimitMiddleware(apiRouter))

	// 🌍 Apply CORS middleware
	corsRouter := setupCORS(withMiddleware)

	http.Handle("/", loggingMiddleware(r))
	log.Println("Server running on port 2000...")
	log.Fatal(http.ListenAndServe(":2000", corsRouter))
}
