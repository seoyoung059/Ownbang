// 로그인 폼
import * as React from "react";

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

// 여기부터 화면에 보이는 로그인 폼 입니다.
const LoginForm = () => {
  const theme = useTheme();
  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    console.log({
      userId: data.get("userId"),
      password: data.get("password"),
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
              로그인 하기
            </Button>

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
