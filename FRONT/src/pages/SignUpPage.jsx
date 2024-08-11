import React from "react";
import { Container, Box, Typography } from "@mui/material";
import { useTheme } from "@mui/material";
import SignUpForm from "../components/sign-up/SignUpForm";

export default function SignUpPage() {
  const theme = useTheme();
  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 12,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Typography
          component="h1"
          variant="h5"
          sx={{ fontWeight: theme.fontWeight.bold, mt: 8 }}
        >
          회원가입
        </Typography>
        <SignUpForm />
      </Box>
    </Container>
  );
}
