package models

type User struct {
	ID   int    `json:"id"`
	Name string `json:"name"`
	// Email           string `json:"email"`
	Password string `json:"password"`
	Email    string `json:"email"`
	// Password        string `json:"password"`
	// Fields stored in JSON but NOT in the database
	ConfirmPassword string `json:"confirm_password" gorm:"-"`
	Address         string `json:"address" gorm:"-"`
	Preferences     string `json:"preferences" gorm:"-"`
	Location        string `json:"location" gorm:"-"`
	Sex             string `json:"sex" gorm:"-"`
	City            string `json:"city" gorm:"-"`
	Interests       string `json:"interests" gorm:"-"`
	Mobile          string `json:"mobile" gorm:"-"`
}
