import React from "react";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { useTheme } from "@mui/material/styles";
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
