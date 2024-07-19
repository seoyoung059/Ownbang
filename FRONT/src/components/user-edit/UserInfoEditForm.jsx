import React, { useState } from "react";
import {
  Container,
  CssBaseline,
  Box,
  Typography,
  Divider,
  Grid,
  TextField,
  InputAdornment,
  Button,
  IconButton,
} from "@mui/material";
import { useTheme } from "@emotion/react";
import StatusChangeForm from "./StatusChangeForm";

// 더미데이터 - 데이터 연결 필요
const user = {
  userName: "김일태",
  phoneNumber: "010-1234-5678",
  userId: "iltae94@gmail.com",
  password: "password123",
};

export default function UserInfoEditForm() {
  const theme = useTheme();
  // 유저 정보
  const [userInfo, setUserInfo] = useState(user);

  const [forAgent, setForAgent] = useState(false);

  // 입력창에 있는 정보들을 userInfo에 반영
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserInfo((prevUserInfo) => ({
      ...prevUserInfo,
      [name]: value,
    }));
  };

  // 전화번호의 경우
  const handlePhoneNumberChange = (event) => {
    let formattedPhoneNumber = event.target.value.replace(/[^\d]/g, ""); // 숫자 이외의 문자 모두 제거

    // 전화번호 형식에 맞춰 '-' 기호 삽입
    if (formattedPhoneNumber.length > 3 && formattedPhoneNumber.length <= 7) {
      formattedPhoneNumber = formattedPhoneNumber.replace(
        /(\d{3})(\d{4})/,
        "$1-$2"
      );
    } else if (formattedPhoneNumber.length > 7) {
      formattedPhoneNumber = formattedPhoneNumber.replace(
        /(\d{3})(\d{4})(\d{4})/,
        "$1-$2-$3"
      );
    }

    setUserInfo((prevUserInfo) => ({
      ...prevUserInfo,
      phoneNumber: formattedPhoneNumber,
    }));
  };

  const handleEdit = (props) => {
    setForAgent(props);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // 여기서 실제로 변경 사항을 서버에 반영하거나 다른 작업 수행
    console.log("유저 정보:", userInfo);
  };

  return (
    <>
      <Container component="main" maxWidth="xs">
        <CssBaseline />

        {!forAgent ? (
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
            <Typography
              sx={{
                textAlign: "end",
                fontSize: theme.fontSize.small,
              }}
              onClick={() => setForAgent((prev) => !prev)}
            >
              중개인 회원 전환 신청
            </Typography>
            <Divider component="div" sx={{ mt: 1, width: "100%" }} />
            <Box component="form" noValidate sx={{ mt: 4 }}>
              <Grid container spacing={4}>
                <Grid item xs={12}>
                  <TextField
                    label="이름"
                    id="userName"
                    name="userName"
                    value={userInfo.userName}
                    onChange={handleInputChange}
                    fullWidth
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="휴대폰 번호"
                    type="text"
                    value={userInfo.phoneNumber}
                    onChange={(handleInputChange, handlePhoneNumberChange)}
                    placeholder="010-1234-5678"
                    inputProps={{
                      maxLength: 13, // 최대 입력 길이 설정 (예: 010-1234-5678)
                    }}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    label="아이디"
                    id="userId"
                    name="userId"
                    type="email"
                    fullWidth
                    value={userInfo.userId}
                    onChange={handleInputChange}
                  />
                </Grid>
              </Grid>
              <Grid
                item
                xs={8}
                sx={{ display: "flex", justifyContent: "space-around" }}
              >
                {/* 취소 버튼 클릭 시 어느 페이지로 이동할 지 */}
                <Button
                  variant="contained"
                  onClick={handleSubmit}
                  sx={{
                    mt: 6,
                    mb: 2,
                    height: "50px",
                    width: "25%",
                    backgroundColor: theme.palette.action.disabled,
                  }}
                >
                  취소
                </Button>
                <Button
                  type="submit"
                  variant="contained"
                  onClick={handleSubmit}
                  sx={{
                    mt: 6,
                    mb: 2,
                    height: "50px",
                    width: "25%",
                    backgroundColor: theme.palette.primary.main,
                  }}
                >
                  확인
                </Button>
              </Grid>
            </Box>
          </Box>
        ) : (
          <StatusChangeForm toggleEdit={handleEdit} />
        )}
      </Container>
    </>
  );
}
