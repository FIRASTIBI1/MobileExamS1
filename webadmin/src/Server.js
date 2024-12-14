const express = require("express");
const nodemailer = require("nodemailer");
const bodyParser = require("body-parser");
const cors = require("cors");

const app = express();
const port = 5000;

// Middleware
app.use(bodyParser.json());
app.use(cors());

// Configure the transporter for SMTP using Outlook
const transporter = nodemailer.createTransport({
  host: "smtp.office365.com",  // Correct SMTP host for Outlook
  port: 587,                   // Use port 587 for TLS
  secure: false,               // Use STARTTLS (false for port 587)
  auth: {
    user: "sawkeji.inc@outlook.com",  // Your Outlook email
    pass: "xmsxoxhmrietjpwb",         // Your app-specific password
  },
});

// Route to send an email
app.post("/api/send-email", async (req, res) => {
  const { name, email, password, role } = req.body;

  // Validate all required fields
  if (!name || !email || !password || !role) {
    return res.status(400).json({ error: "All fields (name, email, password, role) are required" });
  }

  try {
    // Create the HTML content of the email
    const emailContent = `
    <h2>Account Details</h2>
    <p><strong>Full Name:</strong> ${name}</p>
    <p><strong>Email:</strong> ${email}</p>
    <p><strong>Password:</strong> ${password}</p>
    <p><strong>Role:</strong> ${role}</p>
    <p>Your account has been successfully created.</p>
  `;  

    // Send the email
    const info = await transporter.sendMail({
      from: "kaibi05@outlook.com", // Replace with your Outlook email
      to: email,
      subject: "Your Account Details",
      html: emailContent,
    });

    console.log("Email sent:", info.response);
    res.status(200).json({ message: "Email sent successfully." });
  } catch (error) {
    console.error("Error sending email:", error.message);
    res.status(500).json({ error: "Error sending email." });
  }
});

// Start the server
app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}`);
});
