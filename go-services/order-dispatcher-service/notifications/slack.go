package notifications

import (
	"bytes"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"order-dispatcher-service/models"
	"os"
)

// SlackMessage defines the payload for Slack alerts
type SlackMessageOld struct {
	Text      string `json:"text"`
	Username  string `json:"username"`
	IconEmoji string `json:"icon_emoji"`
}

// SlackMessage represents the structure of the Slack message
type SlackMessage struct {
	Username  string                   `json:"username"`
	IconEmoji string                   `json:"icon_emoji"`
	Blocks    []map[string]interface{} `json:"blocks"`
}

// SendSlackAlert sends a message to a Slack channel
func SendSlackAlert(order models.Order, message string) {
	webhookURL := os.Getenv("SLACK_WEBHOOK_URL")
	slackBotName := os.Getenv("SLACK_BOT_NAME")
	if webhookURL == "" {
		log.Println("⚠️ Slack webhook URL not set. Skipping alert.")
		return
	}

	// payload := SlackMessage{
	// 	Text: "*Order Processing !* :\n" +
	// 		"📦 OrderID: `" + strconv.FormatUint(uint64(order.ID), 10) + "`\t" +
	// 		"📦 customerID: `" + strconv.FormatUint(uint64(order.CustomerID), 10) + "`\n" +
	// 		"📦 ProductID: `" + strconv.FormatUint(uint64(order.ProductID), 10) + "`\t" +
	// 		"📦 Quantity: `" + strconv.FormatUint(uint64(order.Quantity), 10) + "`\t" +
	// 		"📦 TotalCost: `" + strconv.FormatUint(uint64(order.TotalCost), 10) + "`\n" +
	// 		"📦 CreatedAt: `" + order.CreatedAt.Format("2006-01-02 15:04:05") + "`\n" +
	// 		"📝 Message: " + message + "",
	// 	Username:  slackBotName,
	// 	IconEmoji: ":ghost",
	// }

	// Create Slack message blocks (Adaptive Card-like)
	payload := SlackMessage{
		Username:  slackBotName,
		IconEmoji: ":ghost",
		Blocks: []map[string]interface{}{
			{"type": "section", "text": map[string]string{"type": "mrkdwn", "text": "*🚀 Order Processing!*\n"}},
			{"type": "section", "fields": []map[string]string{
				{"type": "mrkdwn", "text": fmt.Sprintf("*📦 Order ID:* `%d`", order.ID)},
				{"type": "mrkdwn", "text": fmt.Sprintf("*👤 Customer ID:* `%d`", order.CustomerID)},
				{"type": "mrkdwn", "text": fmt.Sprintf("*🛒 Product ID:* `%d`", order.ProductID)},
				{"type": "mrkdwn", "text": fmt.Sprintf("*🔢 Quantity:* `%d`", order.Quantity)},
				{"type": "mrkdwn", "text": fmt.Sprintf("*💰 Total Cost:* `₹%d`", order.TotalCost)},
				{"type": "mrkdwn", "text": fmt.Sprintf("*⏳ Created At:* `%s`", order.CreatedAt.Format("2006-01-02 15:04:05"))},
			}},
			{"type": "divider"},
			{"type": "section", "text": map[string]string{"type": "mrkdwn", "text": "📝 *Message:* " + message}},
			{"type": "actions", "elements": []map[string]interface{}{
				{
					"type": "button",
					"text": map[string]string{"type": "plain_text", "text": "🔍 View Order"},
					"url":  "http://localhost:4200/#/orders",
				},
			}},
		},
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
