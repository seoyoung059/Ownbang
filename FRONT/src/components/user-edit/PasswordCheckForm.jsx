import React, { useState } from "react";
import {
  Box,
  Grid,
  Typography,
  TextField,
  Button,
  Container,
  CssBaseline,
  Divider,
  IconButton,
  InputAdornment,
} from "@mui/material";
import { useTheme } from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";

export default function PasswordCheckForm({ onPasswordVerified }) {
  const theme = useTheme();

  const [password, setPassword] = useState("");
  const [verificationError, setVerificationError] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const handlePasswordChange = (e) => {
    const value = e.target.value;
    setPassword(() => value);
    // 입력값이 변경될 때마다 verificationError 초기화
    if (verificationError && value === "") {
      setVerificationError(false);
    }
  };

  const handleTogglePasswordVisibility = () => {
    setShowPassword((prev) => !prev);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (password === "password123") {
      onPasswordVerified(true);
    } else {
      setVerificationError(true);
    }
  };

  return (
    <>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: "flex",
            flexDirection: "column",
          }}
        >
          <Typography
            sx={{ mt: 12, pl: 2, fontWeight: theme.fontWeight.bold }}
            component="h1"
            variant="h5"
          >
            개인정보 변경
          </Typography>
          <Divider component="div" sx={{ mt: 2, width: "100%" }} />
          <Typography
            sx={{
              mt: 6,
              color: theme.palette.text.secondary,
              textAlign: "center",
              fontWeight: theme.fontWeight.bold,
            }}
            component="h3"
          >
            개인 정보 조회를 위해서는 인증이 필요합니다.
            <br />
            비밀번호 입력 후 확인 버튼을 클릭해 주세요.
          </Typography>
          <Box component="form" noValidate sx={{ mt: 4 }}>
            <Grid container spacing={4}>
              <Grid item xs={12}>
                <TextField
                  label="기존 비밀번호"
                  name="password"
                  type={showPassword ? "text" : "password"}
                  id="password"
                  fullWidth
                  value={password}
                  onChange={handlePasswordChange}
                  error={verificationError}
                  InputProps={{
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          onClick={handleTogglePasswordVisibility}
                          edge="end"
                        >
                          {!showPassword ? (
                            <VisibilityOff
                              sx={{ color: theme.palette.action.disabled }}
                            />
                          ) : (
                            <Visibility
                              sx={{ color: theme.palette.secondary.main }}
                            />
                          )}
                        </IconButton>
                      </InputAdornment>
                    ),
                  }}
                />
                {verificationError && (
                  <Typography
                    variant="body2"
                    color="error"
                    sx={{
                      mt: 1,
                      textAlign: "end",
                      fontSize: theme.typography.fontSize.small,
                    }}
                  >
                    비밀번호가 일치하지 않습니다.
                  </Typography>
                )}
              </Grid>
            </Grid>
            <Grid
              item
              xs={8}
              sx={{ display: "flex", justifyContent: "center" }}
            >
              <Button
                type="submit"
                variant="contained"
                sx={{
                  mt: 6,
                  mb: 2,
                  height: "50px",
                  width: "25%",
                  backgroundColor: theme.palette.primary.main,
                }}
                onClick={handleSubmit}
              >
                확인
              </Button>
            </Grid>
          </Box>
        </Box>
      </Container>
    </>
  );
}
