package models

// Depricated
// RegisterRequest includes confirmPassword for validation
type RegisterRequest struct {
	ID   int    `json:"id"`
	Name string `json:"name"`
	// Email           string `json:"email"`
	Password string `json:"password"`
	Email    string `json:"email"`
	// Password        string `json:"password"`
	ConfirmPassword string `json:"confirm_password"`
	// Name            string `json:"name"`
	Address     string `json:"address"`
	Preferences string `json:"preferences"`
	Location    string `json:"location"`
	Sex         string `json:"sex"`
	City        string `json:"city"`
	Interests   string `json:"interests"`
	Mobile      string `json:"mobile"`
}
