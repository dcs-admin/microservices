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
var limiter = rate.NewLimiter(1, 5)

// Customer model
type Customer struct {
	ID      int    `json:"id"`
	Name    string `json:"name" validate:"required"`
	Email   string `json:"email" validate:"required,email"`
	Address string `json:"address" validate:"required"`
	City    string `json:"city" validate:"required"`
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
	//dsn := "root:p2wd1234@123@tcp(localhost:3306)/customer?charset=utf8mb4&parseTime=True&loc=Local"
	//var err error
	db, err = gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Fatal("Failed to connect to database:", err)
	}

	// AutoMigrate the Customer table
	db.AutoMigrate(&Customer{})
	logrus.Info("Database  initiated!")
}

// Create a New Customer (POST /customers)
func CreateCustomer(w http.ResponseWriter, r *http.Request) {
	var customer Customer
	if err := json.NewDecoder(r.Body).Decode(&customer); err != nil {
		http.Error(w, "Invalid request payload", http.StatusBadRequest)
		return
	}

	if err := db.Create(&customer).Error; err != nil {
		http.Error(w, "Error saving customer", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(customer)
	customerJSON, _ := json.Marshal(customer)
	logrus.Info("Customer created: " + string(customerJSON))
}

// Get All Customers (GET /customers)
func GetAllCustomers(w http.ResponseWriter, r *http.Request) {
	var customers []Customer
	db.Find(&customers)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(customers)

	customerJSON, _ := json.Marshal(customers)
	logrus.Info("GetAllCustomers: Customers List " + string(customerJSON))
}

// Get Customer by ID (GET /customers/{id})
func GetCustomerByID(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	var customer Customer
	if err := db.First(&customer, params["id"]).Error; err != nil {
		http.Error(w, "Customer not found", http.StatusNotFound)
		return
	}
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(customer)

	customerJSON, _ := json.Marshal(customer)
	logrus.Info("GetCustomerByID:: " + string(customerJSON))
}

// Update Customer (PUT /customers/{id})
func UpdateCustomer(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	var customer Customer

	// Check if customer exists
	if err := db.First(&customer, params["id"]).Error; err != nil {
		http.Error(w, "Customer not found", http.StatusNotFound)
		return
	}

	// Decode request body
	if err := json.NewDecoder(r.Body).Decode(&customer); err != nil {
		http.Error(w, "Invalid request payload", http.StatusBadRequest)
		return
	}

	db.Save(&customer)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(customer)

	customerJSON, _ := json.Marshal(customer)
	logrus.Info("UpdateCustomer:: " + string(customerJSON))
}

// Delete Customer (DELETE /customers/{id})
func DeleteCustomer(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	var customer Customer

	// Check if customer exists
	if err := db.First(&customer, params["id"]).Error; err != nil {
		http.Error(w, "Customer not found", http.StatusNotFound)
		return
	}

	db.Delete(&customer)
	w.WriteHeader(http.StatusNoContent)

	customerJSON, _ := json.Marshal(customer)
	logrus.Info("DeleteCustomer:: " + string(customerJSON))
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

	//r.Handle("/api/customers", authMiddleware(rateLimitMiddleware(http.HandlerFunc(createCustomer)))).Methods("POST")

	// Group routes under a subrouter to apply middleware globally
	apiRouter := r.PathPrefix("/api").Subrouter()
	// Define Routes
	apiRouter.HandleFunc("/token", generateToken).Methods("GET")
	apiRouter.HandleFunc("/customers", CreateCustomer).Methods("POST")
	apiRouter.HandleFunc("/customers", GetAllCustomers).Methods("GET")
	apiRouter.HandleFunc("/customers/{id}", GetCustomerByID).Methods("GET")
	apiRouter.HandleFunc("/customers/{id}", UpdateCustomer).Methods("PUT")
	apiRouter.HandleFunc("/customers/{id}", DeleteCustomer).Methods("DELETE")

	// Apply Middlewares globally
	withMiddleware := authMiddleware(rateLimitMiddleware(apiRouter))

	http.Handle("/", loggingMiddleware(r))
	log.Println("Server running on port 1000...")
	log.Fatal(http.ListenAndServe(":1000", handlers.CORS(handlers.AllowedOrigins([]string{"*"}))(withMiddleware)))
}
