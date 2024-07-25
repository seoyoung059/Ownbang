import * as React from "react";
import { Container, Box, Typography } from "@mui/material";
import { useTheme } from "@mui/material";
import LoginForm from "../components/login/LoginForm";

export default function LoginPage() {
  const theme = useTheme();
  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
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
          로그인
        </Typography>
        <LoginForm />
      </Box>
    </Container>
  );
}
