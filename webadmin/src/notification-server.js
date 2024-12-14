const express = require("express");
const admin = require("firebase-admin");
const bodyParser = require("body-parser");

// Initialize Firebase Admin SDK
const serviceAccount = require("./kaibiabsence-firebase-adminsdk-81gdw-cf8685833a.json");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
});

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware for parsing JSON
app.use(bodyParser.json());

// API endpoint to send a notification
app.post("/send-notification", (req, res) => {
    const { fcmToken, title, body } = req.body;

    if (!fcmToken || !title || !body) {
        return res.status(400).json({ error: "Missing required fields: fcmToken, title, body" });
    }

    const message = {
        token: fcmToken,
        notification: {
            title: title,
            body: body,
        },
    };

    admin.messaging()
        .send(message)
        .then((response) => {
            console.log("Notification sent successfully:", response);
            res.status(200).json({ success: true, response });
        })
        .catch((error) => {
            console.error("Error sending notification:", error);
            res.status(500).json({ success: false, error });
        });
});

// Start the server
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
