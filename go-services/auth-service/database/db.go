package database

import (
	"fmt"
	"log"
	"os"

	"auth-service/models"

	"github.com/joho/godotenv"
	"github.com/sirupsen/logrus"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

var DB *gorm.DB

// Initialize DB Connection
func InitDB() {
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
	DB, err = gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Fatal("Failed to connect to database:", err)
	}

	// AutoMigrate the Product table
	DB.AutoMigrate(&models.User{})
	logrus.Info("Auth Database  initiated!")
}
