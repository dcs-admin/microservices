package handlers

import (
	"auth-service/database"
	"auth-service/middleware"
	"auth-service/models"
	"auth-service/utils"
	"encoding/json"
	"errors"
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
