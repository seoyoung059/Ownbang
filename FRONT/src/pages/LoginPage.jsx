import * as React from "react";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Link from "@mui/material/Link";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { useTheme } from "@mui/material/styles";
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
          style={{ fontWeight: theme.fontWeight.bold }}
          sx={{ mt: 8 }}
          component="h1"
          variant="h5"
        >
          로그인
        </Typography>
        <LoginForm />
      </Box>
    </Container>
  );
}
