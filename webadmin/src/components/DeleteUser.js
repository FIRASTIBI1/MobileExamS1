import React, { useState } from "react";
import { TextField, Button, Box, Typography } from "@mui/material";
import { auth, db } from "../firebase";
import { deleteUser } from "firebase/auth";
import { collection, query, where, getDocs, deleteDoc } from "firebase/firestore";

const DeleteUser = () => {
  const [email, setEmail] = useState("");

  const handleDeleteUser = async () => {
    try {
      if (!email) {
        alert("Please provide a valid email address.");
        return;
      }

      // Find the user in Firestore based on email
      const usersCollection = collection(db, "users");
      const userQuery = query(usersCollection, where("email", "==", email));
      const querySnapshot = await getDocs(userQuery);

      if (querySnapshot.empty) {
        alert("No user found with the provided email.");
        return;
      }

      // Assuming there's only one user with this email
      const userDoc = querySnapshot.docs[0];

      // Delete the user document from Firestore
      await deleteDoc(userDoc.ref);

      // Optionally, delete the user from Firebase Authentication
      const user = auth.currentUser; // This works only for the current user if logged in
      if (user && user.email === email) {
        await deleteUser(user);
      }

      alert("User deleted successfully!");
      setEmail(""); // Clear input field
    } catch (error) {
      console.error("Error deleting user:", error.message);
      alert("Failed to delete user: " + error.message);
    }
  };

  return (
    <Box my={4} p={3} border={1} borderRadius={2} borderColor="grey.300">
      <Typography variant="h6" gutterBottom>
        Delete User by Email
      </Typography>
      <TextField
        label="Email"
        fullWidth
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        helperText="Enter the email of the user to delete"
        margin="normal"
      />
      <Button
        variant="contained"
        color="secondary"
        onClick={handleDeleteUser}
        disabled={!email}
      >
        Delete User
      </Button>
    </Box>
  );
};

export default DeleteUser;
