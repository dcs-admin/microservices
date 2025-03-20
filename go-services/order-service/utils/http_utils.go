package utils

import (
	"bytes"
	"encoding/json"
	"errors"
	"log"
	"net/http"
	"order-service/constants"
	"time"

	"github.com/gin-gonic/gin"
)

func MakeGETRequest(url, token string) (map[string]interface{}, error) {
	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return nil, err
	}
	req.Header.Set("Authorization", "Bearer "+token)

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, errors.New("service returned non-200 status")
	}

	var response map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&response); err != nil {
		return nil, err
	}
	return response, nil
}
func RequestToken(url string, c *gin.Context) (constants.TokenResponse, error) {
	userID, email, err := getUserDetailsFromContext(c)
	if err != nil {
		return constants.TokenResponse{}, err
	}

	reqBody := map[string]interface{}{
		"id":    userID,
		"email": email,
		"user_data": map[string]interface{}{
			"role": "admin",
			"age":  30,
		},
	}

	body, _ := json.Marshal(reqBody)
	req, err := http.NewRequest("POST", url, bytes.NewBuffer(body))
	if err != nil {
		return constants.TokenResponse{}, err
	}
	req.Header.Set("Content-Type", "application/json")

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return constants.TokenResponse{}, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return constants.TokenResponse{}, errors.New("token service returned error")
	}

	var tokenResponse constants.TokenResponse
	if err := json.NewDecoder(resp.Body).Decode(&tokenResponse); err != nil {
		return constants.TokenResponse{}, err
	}

	if tokenResponse.Exp == 0 {
		log.Println("🚨 Token API did not return expiration time! Defaulting to 15 minutes.")
		tokenResponse.Exp = time.Now().Add(15 * time.Minute).Unix()
	}

	return tokenResponse, nil
}

func getUserDetailsFromContext(c *gin.Context) (int, string, error) {
	userID, _ := c.Get("user_id")
	email, _ := c.Get("email")
	return userID.(int), email.(string), nil
}
