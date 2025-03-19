package handlers

import (
	"auth-service/database"
	"auth-service/middleware"
	"auth-service/models"
	"auth-service/utils"
	"bytes"
	"encoding/json"
	"errors"
	"log"
	"net/http"

	"gorm.io/gorm"
)

// RegisterHandler handles user registration
func RegisterHandler(w http.ResponseWriter, r *http.Request) {
	var user models.User
	json.NewDecoder(r.Body).Decode(&user)

	hashedPassword, err := utils.HashPassword(user.Password)
	if err != nil {
		http.Error(w, "Error hashing password", http.StatusInternalServerError)
		return
	}
	user.Password = hashedPassword

	// err = database.DB.QueryRow("INSERT INTO users (name, email, password) VALUES ($1, $2, $3) RETURNING id",
	// 	user.Name, user.Email, hashedPassword).Scan(&user.ID)
	// if err != nil {
	// 	http.Error(w, "User already exists", http.StatusConflict)
	// 	return
	// }

	if err := database.DB.Create(&user).Error; err != nil {
		http.Error(w, "User already exists", http.StatusInternalServerError)
		return
	}

	// Call customer-service to save additional details
	go createCustomerInService(user)

	json.NewEncoder(w).Encode(map[string]string{"message": "User registered successfully"})
}

// LoginHandler handles user login
func LoginHandler(w http.ResponseWriter, r *http.Request) {
	var user models.User
	var input models.User // Store input credentials

	// Decode JSON request body into 'input'
	if err := json.NewDecoder(r.Body).Decode(&input); err != nil {
		http.Error(w, "Invalid request payload", http.StatusBadRequest)
		return
	}

	// Fetch user from database using Email
	err := database.DB.Where("email = ?", input.Email).First(&user).Error
	if err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			http.Error(w, "Invalid email", http.StatusUnauthorized)
			return
		}
		http.Error(w, "Database error", http.StatusInternalServerError)
		return
	}

	// Verify password
	if err := utils.CheckPassword(user.Password, input.Password); err != nil {
		http.Error(w, "Invalid credentials, password does not match.", http.StatusUnauthorized)
		return
	}

	// Generate JWT token
	token, _ := middleware.GenerateJWT(user.Email, user.ID)

	// Remove sensitive data before sending the response
	user.Password = ""

	// Send back token and full user object
	json.NewEncoder(w).Encode(map[string]interface{}{
		"token": token,
		"user":  user,
	})
}

// createCustomerInService sends user details to customer-service (port 1000)
func createCustomerInService(req models.User) {
	customerData := map[string]interface{}{
		"email":       req.Email, // Email as the main userID
		"name":        req.Name,
		"address":     req.Address,
		"preferences": req.Preferences,
		"location":    req.Location,
		"sex":         req.Sex,
		"city":        req.City,
		"interests":   req.Interests,
		"mobile":      req.Mobile,
		"id":          req.ID,
	}

	jsonData, _ := json.Marshal(customerData)

	resp, err := http.Post("http://localhost:1000/api/customers", "application/json", bytes.NewBuffer(jsonData))
	if err != nil {
		log.Println("Error sending data to customer-service:", err)
		return
	}
	defer resp.Body.Close()

	log.Println("customer-service response:", resp.Status)
}
