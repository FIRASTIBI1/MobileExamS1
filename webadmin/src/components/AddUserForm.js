import React from "react";
import { Formik, Form, Field } from "formik";
import * as Yup from "yup";
import { TextField, Button, Box, Typography } from "@mui/material";
import { auth, db } from "../firebase";
import { createUserWithEmailAndPassword } from "firebase/auth";
import { doc, setDoc } from "firebase/firestore";

const AddUserForm = () => {
  // Validation schema to ensure all fields are valid
  const validationSchema = Yup.object({
    name: Yup.string().required("Name is required"),
    email: Yup.string().email("Invalid email").required("Email is required"),
    password: Yup.string()
      .min(6, "Password must be at least 6 characters")
      .required("Password is required"),
    role: Yup.string().required("Role is required"), // Validate role, e.g., "teacher" or "admin"
  });

  // Function to handle adding a user
  const handleAddUser = async (values, { resetForm }) => {
    try {
      // Create user in Firebase Auth
      const userCredential = await createUserWithEmailAndPassword(
        auth,
        values.email,
        values.password
      );

      // Add user details to Firestore
      await setDoc(doc(db, "users", userCredential.user.uid), {
        name: values.name,
        email: values.email,
        role: values.role,
      });

      alert("User added successfully!");
      resetForm();
    } catch (error) {
      console.error("Error adding user:", error.message);
      alert("Failed to add user: " + error.message); // Show specific error message
    }
  };

  return (
    <Box my={4} p={3} border={1} borderRadius={2} borderColor="grey.300">
      <Typography variant="h6" gutterBottom>
        Add New User
      </Typography>
      <Formik
        initialValues={{
          name: "",
          email: "",
          password: "",
          role: "Teacher", // Default role
        }}
        validationSchema={validationSchema}
        onSubmit={handleAddUser}
      >
        {({ errors, touched, isSubmitting }) => (
          <Form>
            <Box mb={2}>
              <Field
                as={TextField}
                name="name"
                label="Name"
                fullWidth
                error={touched.name && Boolean(errors.name)}
                helperText={touched.name && errors.name}
              />
            </Box>
            <Box mb={2}>
              <Field
                as={TextField}
                name="email"
                label="Email"
                type="email"
                fullWidth
                error={touched.email && Boolean(errors.email)}
                helperText={touched.email && errors.email}
              />
            </Box>
            <Box mb={2}>
              <Field
                as={TextField}
                name="password"
                label="Password"
                type="password"
                fullWidth
                error={touched.password && Boolean(errors.password)}
                helperText={touched.password && errors.password}
              />
            </Box>
            <Box mb={2}>
              <Field
                as={TextField}
                name="role"
                label="Role"
                fullWidth
                helperText="e.g., Admin, Agent, Teacher"
              />
            </Box>
            <Button
              type="submit"
              variant="contained"
              color="primary"
              disabled={isSubmitting}
            >
              {isSubmitting ? "Adding..." : "Add User"}
            </Button>
          </Form>
        )}
      </Formik>
    </Box>
  );
};

export default AddUserForm;
