package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"strings"
	"sync"
	"sync/atomic"
	"strconv"
	"html"
	"html/template" 
	"github.com/gorilla/csrf"
	"github.com/gorilla/mux" 
	"time" 
	"github.com/dgrijalva/jwt-go"
	"golang.org/x/time/rate"
)

// Define User struct
type User struct {
	ID		int64   `json:"id"`
	Name    string `json:"name"`
	Email   string `json:"email"`
	Address string `json:"address"`
}

// In-memory user storage
var users []User
var mu sync.Mutex // Mutex for thread-safe operations

var AUTO_INCR_ID int64 = 0 // Global auto-increment variable

type MyCustomResponse struct {
	Body      string   `json:"body"`
	Response  []string `json:"response"`
	ErrorCode int      `json:"errorCode"`
	ErrorMsg  string   `json:"errorMsg"`
}

// Define a struct for the API response
type Response struct {
	Message string `json:"message"`
}

// Home handler
// HomeHandler - Serves the form with the CSRF token
func HomeHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/html")
	csrfToken := csrf.Token(r) // Extract CSRF token
	fmt.Println("csrfToken:: "+csrfToken)
	html := fmt.Sprintf(`<h2> My Form </h2><form action="/submit" method="POST">
		<input type="hidden" name="gorilla.csrf.Token" value="%s">
		<input type="text" name="name">
		<button type="submit">Submit</button>
	</form>`, csrfToken)

	w.Write([]byte(html))
}

// SubmitHandler - Handles form submission with CSRF token
func SubmitHandler(w http.ResponseWriter, r *http.Request) {
	name := r.FormValue("name")
	w.Write([]byte("Received: " + name))
}

// Get user by ID
func GetUserHandler(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
  
	userID, err := strconv.ParseInt(vars["id"], 10, 64) // Base 10, 64-bit integer
	if err != nil {
		fmt.Println("Error:", err)
	} else {
		fmt.Println("Converted Integer:", userID)
	}

	mu.Lock()
	defer mu.Unlock()

	for _, user := range users {
		if user.ID == userID {
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(user)
			return
		}
	}

	// If user not found
	//http.Error(w, "User not found with ID: "+ strconv.FormatInt(userID, 10), http.StatusNotFound)
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader( http.StatusNotFound)

	// Create error response
	errorResponse := map[string]string{"error": "User not found with ID: "+ strconv.FormatInt(userID, 10)}

	// Encode and send JSON response
	json.NewEncoder(w).Encode(errorResponse)
	 
}

// Get user by ID
func GetAllUsers(w http.ResponseWriter, r *http.Request) { 
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(users)
}


// Get User List
func GetUserListHandler(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	userID := vars["id"]
	list := []string{"1","2","3"}
	str := strings.Join(list, ",") 
	

	// Create a response object
	response := MyCustomResponse{
		Body:      "Given Anji User ID: " + userID + " ; List: " + str,
		Response:  list,
		ErrorCode: 0,
		ErrorMsg:  "",
	}
	//response := Response{Message: MyCustomResponse }
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

 



// SaveUser function
func SaveUser(w http.ResponseWriter, r *http.Request) {
	// Parse JSON request body
	var newUser User
	if err := json.NewDecoder(r.Body).Decode(&newUser); err != nil {
		http.Error(w, "Invalid request payload", http.StatusBadRequest)
		return
	}

	newUser.ID = atomic.AddInt64(&AUTO_INCR_ID, 1)

	// Add user to list safely
	mu.Lock()
	users = append(users, newUser)
	mu.Unlock()

	// Create a response struct
	response := map[string]interface{}{
		"message": "User saved successfully: ID " + strconv.FormatInt(newUser.ID, 10),
		"user":    newUser,
	}

	// Send response
	w.Header().Set("Content-Type", "application/json")
	// Set Content-Type as JSON
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated) // 201 Created

	// Encode response to JSON and send it
	json.NewEncoder(w).Encode(response)}

// FindUser function (Get user by ID)
func FindUser(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	//var userID int = vars["id"]
	userID, err := strconv.ParseInt(vars["id"], 10, 64) // Base 10, 64-bit integer
	if err != nil {
		fmt.Println("Error:", err)
	} else {
		fmt.Println("Converted Integer:", userID)
	}

	mu.Lock()
	defer mu.Unlock()

	for _, user := range users {
		if user.ID == userID {
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(user)
			return
		}
	}

	// If user not found
	http.Error(w, "User not found", http.StatusNotFound)
}


func XSSHandler(w http.ResponseWriter, r *http.Request) {
	tmpl := template.Must(template.New("example").Parse(`<h1>{{ . }}</h1>`))
	
	// Get the 'name' parameter from the URL
	name := r.URL.Query().Get("name")

	// Execute the template and send output to ResponseWriter
	err := tmpl.Execute(w, name)
	if err != nil {
		http.Error(w, "Internal Server Error", http.StatusInternalServerError)
		return
	}

	// Print to console for debugging (not for client output)
	// Escape before logging to prevent XSS in logs
	safeName := html.EscapeString(name)
	fmt.Printf("XSS handled Name: %s\n", safeName)
}

// Generate JWT token
func generateToken(w http.ResponseWriter, r *http.Request) {
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, jwt.MapClaims{
		"username": "user123",
		"exp":      time.Now().Add(time.Hour * 2).Unix(),
	})
	tokenString, _ := token.SignedString(secretKey)
	w.Write([]byte(tokenString))
}

// Middleware to verify JWT
func verifyToken(next http.HandlerFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		tokenString := r.Header.Get("Authorization")
		if tokenString == "" {
			http.Error(w, "Unauthorized- Eithe an Invalid or empty token passed from Authorization Header", http.StatusUnauthorized)
			return
		}

		token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
			return secretKey, nil
		})

		if err != nil || !token.Valid {
			http.Error(w, "Invalid token", http.StatusUnauthorized)
			return
		}

		next.ServeHTTP(w, r)
	}
}

func protectedRoute(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("This is a protected route"))
}


func securityHeadersMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("X-Frame-Options", "DENY")
		w.Header().Set("X-Content-Type-Options", "nosniff")
		w.Header().Set("X-XSS-Protection", "1; mode=block")
		w.Header().Set("Content-Security-Policy", "default-src 'self'")
		next.ServeHTTP(w, r)
	})
}

func rateLimitedHandler(w http.ResponseWriter, r *http.Request) {
	if !limiter.Allow() {
		http.Error(w, "Too many requests", http.StatusTooManyRequests)
		return
	}
	fmt.Fprintln(w, "Request successful")
}

// CSRF protection middleware
var csrfKey = []byte("MTc0MTg1MTg0NXxJa2xVY1hOVmNrWnhTWGQ1U1VSdmRqWTNZa3BNYVRsWVdGbGlVemR3TWsxb1ltbENUa1U0YkZNMlZGVTlJZ289fE07KlEGjYRED5UbyhiM_l6vVI33sYzVhU_TpR54Uy7Q")
var secretKey = []byte("MTc0MTg1MTg0NXxJa2xVY1hOVmNrWnhTWGQ1U1VSdmRqWTNZa3BNYVRsWVdGbGlVemR3TWsxb1ltbENUa1U0YkZNMlZGVTlJZ289fE07KlEGjYRED5UbyhiM_l6vVI33sYzVhU_TpR54Uy7Q")
var limiter = rate.NewLimiter(1, 5) // 1 request per second, burst up to 5


func main() {
	r := mux.NewRouter()

	// CSRF Middleware
	csrfMiddleware := csrf.Protect(csrfKey, csrf.Secure(false))

	// Routes that need CSRF protection
	protected := r.PathPrefix("/").Subrouter()
	protected.Use(csrfMiddleware)
	protected.HandleFunc("/", HomeHandler).Methods("GET")
	protected.HandleFunc("/submit", SubmitHandler).Methods("POST")

	// Routes that should NOT have CSRF protection
	r.HandleFunc("/users", SaveUser).Methods("POST")  // No CSRF
	r.HandleFunc("/users", GetAllUsers).Methods("GET")
	r.HandleFunc("/users/{id}", GetUserHandler).Methods("GET")
	r.HandleFunc("/users/{id}/listsites", GetUserListHandler).Methods("GET")

	//XSS
	r.HandleFunc("/xss", XSSHandler)

	//UAA
	r.HandleFunc("/api/token", generateToken)
	r.HandleFunc("/api/protected", verifyToken(protectedRoute))
	r.HandleFunc("/api/protected/users", verifyToken(GetAllUsers))
	r.HandleFunc("/api/protected/users/{id}", verifyToken(GetUserHandler))

	//Rate Limiter
	r.HandleFunc("/api/limited", rateLimitedHandler)

	// Start server with CSRF middleware applied only to protected routes
	fmt.Println("Server running on port 1234...")
	log.Fatal(http.ListenAndServe(":1234", securityHeadersMiddleware(r))) // CSRF only on protected routes
}




// Web Attacks
// CSRF - protected - /submit
// XSS - http://localhost:1234/xss?name=%3Cscript%3E%20alert(%27Anji%27)%3C/script%3E
// CORS - Simple Request cors with allowed origins and http methods
// JWT Token Authorization
//Rate Limiting
