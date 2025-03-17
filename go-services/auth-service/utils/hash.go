package utils

import (
	"errors"
	"log"

	"golang.org/x/crypto/bcrypt"
)

// HashPassword hashes a given password
func HashPassword(password string) (string, error) {
	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	return string(hashedPassword), err
}

// CheckPassword compares a hashed password with plain text
func CheckPassword(hashedPassword, password string) error {
	if password == "" {
		log.Println("Password check failed: empty password provided")
		return errors.New("password cannot be empty")
	}

	log.Println("Password check:", hashedPassword, password)
	return bcrypt.CompareHashAndPassword([]byte(hashedPassword), []byte(password))
}
