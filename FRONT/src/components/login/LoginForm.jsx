import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import KakaoLoginButton from "./KakaoLoginButton";

import { useBoundStore } from "../../store/store";

import {
  CssBaseline,
  Box,
  TextField,
  Button,
  Link,
  Grid,
  Typography,
  Container,
  keyframes,
  InputAdornment,
  IconButton,
} from "@mui/material";

import { Visibility, VisibilityOff } from "@mui/icons-material";

import { useTheme, ThemeProvider } from "@mui/material/styles";

function Copyright(props) {
  return (
    <Typography
      variant="body2"
      color="text.secondary"
      align="center"
      {...props}
    >
      {"Copyright © "}
      <Link color="inherit" href="https://mui.com/">
        Your Website
      </Link>{" "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

const shake = keyframes`
  0%, 100% { transform: translateX(0); }
  10%, 30%, 50%, 70%, 90% { transform: translateX(-5px); }
  20%, 40%, 60%, 80% { transform: translateX(5px); }
`;

const LoginForm = () => {
  const { loginUser, fetchUser } = useBoundStore((state) => ({
    loginUser: state.loginUser,
    fetchUser: state.fetchUser,
  }));
  const theme = useTheme();
  const navigate = useNavigate();
  const [loginFail, setLoginFail] = useState("");
  const [shakeAnimation, setShakeAnimation] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const handleTogglePasswordVisibility = () => {
    setShowPassword((prev) => !prev);
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    loginUser({
      email: data.get("userId"),
      password: data.get("password"),
    })
      .then((res) => {
        if (res.resultCode === "SUCCESS") {
          toast.success("로그인 성공", {
            position: "bottom-left",
            autoClose: 2000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "light",
          });
          fetchUser();
          navigate("/");
        }
      })
      .catch((err) => {
        setLoginFail(err.response.data.message);
        setShakeAnimation(true);
        setTimeout(() => setShakeAnimation(false), 300);
      });
  };

  const handleChange = () => {
    if (loginFail) {
      setLoginFail("");
    }
  };

  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 4,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Box
            component="form"
            onSubmit={handleSubmit}
            noValidate
            sx={{ mt: 1 }}
          >
            <TextField
              label="이메일"
              id="userId"
              name="userId"
              margin="normal"
              autoComplete="userId"
              required
              fullWidth
              autoFocus
              onChange={handleChange}
            />
            <TextField
              label="비밀번호"
              type={showPassword ? "text" : "password"}
              id="password"
              name="password"
              margin="normal"
              autoComplete="current-password"
              required
              fullWidth
              onChange={handleChange}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      onClick={handleTogglePasswordVisibility}
                      edge="end"
                    >
                      {showPassword ? (
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
            <Box sx={{ textAlign: "end", mr: 1, mt: 1 }}>
              {loginFail && (
                <Typography
                  sx={{
                    fontSize: theme.fontSize.small,
                    color: "red",
                    animation: shakeAnimation ? `${shake} 0.3s` : "none",
                  }}
                >
                  {loginFail}
                </Typography>
              )}
            </Box>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{
                mt: 4,
                mb: 2,
                height: "50px",
                backgroundColor: theme.palette.primary.main,
              }}
            >
              로그인
            </Button>
            <KakaoLoginButton />
            <Grid container>
              <Grid item xs>
                <Link href="#" variant="body2">
                  비밀번호 찾기
                </Link>
              </Grid>
              <Grid item>
                <Link href="/signup" variant="body2">
                  아직 계정이 없으신가요?
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
        {/* <Copyright sx={{ mt: 8, mb: 4 }} /> */}
      </Container>
    </ThemeProvider>
  );
};

export default LoginForm;
