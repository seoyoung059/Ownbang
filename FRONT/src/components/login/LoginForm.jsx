// 로그인 폼
import * as React from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
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
} from "@mui/material";

import { useTheme, ThemeProvider } from "@mui/material/styles";

// SignUp과 마찬가지로 카피라이트 작성 시 맨 아래 코드 활성화 하면 됩니다.
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

const LoginForm = () => {
  const { loginUser } = useBoundStore((state) => ({
    loginUser: state.loginUser,
  }));
  const theme = useTheme();
  const navigate = useNavigate();
  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    console.log({
      email: data.get("userId"),
      password: data.get("password"),
    });
    loginUser({
      email: data.get("userId"),
      password: data.get("password"),
    }).then((res) => {
      // 로그인 성공 시 TOAST 실패 시 다른 처리 추가
      if (res.resultCode === "SUCCESS") {
        toast.success(res.message, {
          position: "bottom-left",
          autoClose: 2000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "light",
        });
        navigate("/");
      }
    });
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
              label="아이디"
              id="userId"
              name="userId"
              margin="normal"
              autoComplete="userId"
              required
              fullWidth
              autoFocus
            />
            <TextField
              label="비밀번호"
              type="password"
              id="password"
              name="password"
              margin="normal"
              autoComplete="current-password"
              required
              fullWidth
            />
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

            {/* 로그인 하기 버튼 하단에 위치한 부가 기능 */}
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
