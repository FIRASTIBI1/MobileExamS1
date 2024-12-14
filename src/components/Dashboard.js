import React from "react";
import { Container, AppBar, Toolbar, Typography, Box } from "@mui/material";
import UserList from "./UserList";
import AddUserForm from "./AddUserForm";
import AddSalleEmploi from "./AddSalleEmploi"; // Import the new AddSalleEmploi component
import DeleteUser from "./DeleteUser"; // Import the DeleteUser component
import ClaimsList from "./ClaimsList";

const Dashboard = () => {
  return (
    <Container>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6">Admin Dashboard</Typography>
        </Toolbar>
      </AppBar>
      <Box my={4}>
        <UserList />
      </Box>
      <Box my={4}>
        <AddUserForm />
      </Box>
      <Box my={4}>
        <AddSalleEmploi /> {/* Add the AddSalleEmploi component */}
      </Box>
      <Box my={4}>
        <DeleteUser /> {/* Add the DeleteUser component */}
      </Box>
      <Box my={4}>
        <ClaimsList />
      </Box>
    </Container>
  );
};

export default Dashboard;
