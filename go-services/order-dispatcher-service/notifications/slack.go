package notifications

import (
	"bytes"
	"encoding/json"
	"log"
	"net/http"
	"order-dispatcher-service/models"
	"os"
	"strconv"
)

// SlackMessage defines the payload for Slack alerts
type SlackMessage struct {
	Text      string `json:"text"`
	Username  string `json:"username"`
	IconEmoji string `json:"icon_emoji"`
}

// SendSlackAlert sends a message to a Slack channel
func SendSlackAlert(order models.Order, message string) {
	webhookURL := os.Getenv("SLACK_WEBHOOK_URL")
	slackBotName := os.Getenv("SLACK_BOT_NAME")
	if webhookURL == "" {
		log.Println("⚠️ Slack webhook URL not set. Skipping alert.")
		return
	}

	payload := SlackMessage{
		Text: "*Order Processing !* :\n" +
			"📦 OrderID: `" + strconv.FormatUint(uint64(order.ID), 10) + "`\t" +
			"📦 customerID: `" + strconv.FormatUint(uint64(order.CustomerID), 10) + "`\n" +
			"📦 ProductID: `" + strconv.FormatUint(uint64(order.ProductID), 10) + "`\t" +
			"📦 Quantity: `" + strconv.FormatUint(uint64(order.Quantity), 10) + "`\t" +
			"📦 TotalCost: `" + strconv.FormatUint(uint64(order.TotalCost), 10) + "`\n" +
			"📦 CreatedAt: `" + order.CreatedAt.Format("2006-01-02 15:04:05") + "`\n" +
			"📝 Message: " + message + "",
		Username:  slackBotName,
		IconEmoji: ":ghost",
	}

	data, _ := json.Marshal(payload)

	resp, err := http.Post(webhookURL, "application/json", bytes.NewBuffer(data))
	if err != nil {
		log.Println("❌ Failed to send Slack alert:", err)
		return
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		log.Println("⚠️ Slack returned non-200 response:", resp.Status)
	} else {
		log.Println("✅ Slack alert sent successfully!")
	}
}
