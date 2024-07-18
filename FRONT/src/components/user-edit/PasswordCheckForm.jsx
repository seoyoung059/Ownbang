// 마이페이지에서 개인 정보 변경으로 이동 시 비밀번호를 다시 체크하는 컴퍼넌트
import React from "react";

import {
  Box,
  Grid,
  Typography,
  TextField,
  Button,
  Container,
  CssBaseline,
  Divider,
} from "@mui/material";

import { useTheme } from "@mui/material";

export default function PasswordCheckForm() {
  const theme = useTheme();

  return (
    <>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: "flex",
            flexDirection: "column",
            // alignItems: "center",
          }}
        >
          <Typography
            style={{ fontWeight: theme.fontWeight.bold }}
            sx={{ mt: 8, pl: 2 }}
            component="h1"
            variant="h5"
          >
            개인정보 변경
          </Typography>
          <Divider component="div" sx={{ mt: 2, width: "100%" }} />
          <></>
          <Typography
            style={{ fontWeight: theme.fontWeight.bold }}
            sx={{
              mt: 6,
              color: theme.palette.text.secondary,
              textAlign: "center",
            }}
            component="h3"
          >
            개인 정보 조회를 위해서는 인증이 필요합니다.
            <br />
            비밀번호 입력 후 확인 버튼을 클릭해 주세요.
          </Typography>
          <Box component="form" noValidate sx={{ mt: 8 }}>
            <Grid container spacing={4}>
              <Grid item xs={12}>
                <TextField
                  label="기존 비밀번호"
                  name="password"
                  type="password"
                  id="password"
                  required
                  fullWidth
                />
                {/* 비밀번호 입력 란 하단에 위치한 안내 문구 */}
                <Typography
                  name="pwCheck"
                  style={{
                    fontSize: theme.fontSize.small,
                    marginTop: 10,
                    marginLeft: 10,
                  }}
                >
                  9~16자리 영문 대소문자, 숫자, 특수문자 조합
                </Typography>
              </Grid>
            </Grid>
            <></>
            <Grid
              item
              xs={8}
              sx={{ display: "flex", justifyContent: "center" }}
            >
              <Button
                type="submit"
                variant="contained"
                sx={{ mt: 6, mb: 2, height: "50px", width: "25%" }}
                style={{ backgroundColor: theme.palette.primary.main }}
              >
                확인
              </Button>
            </Grid>
          </Box>
        </Box>
        {/* <Copyright sx={{ mt: 5 }} /> */}
      </Container>
    </>
  );
}
