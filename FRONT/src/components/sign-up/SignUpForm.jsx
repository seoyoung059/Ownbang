import React, { useState, useEffect } from "react";
import {
  CssBaseline,
  Button,
  TextField,
  Link,
  Grid,
  Box,
  Typography,
  Container,
} from "@mui/material";
import { useTheme } from "@mui/material";
import { useBoundStore } from "../../store/store";

// 화면 하단에 사용하는 copyright인데 맨 아래 코드를 비활성화 시켜둠.
function Copyright(props) {
  return (
    <Typography
      variant="body2"
      color="theme.palette.text.secondary"
      align="center"
      {...props}
    >
      {"Copyright © "}
      <Link color="inherit" href="https://mui.com/">
        OwnBang-SSAFYA702
      </Link>{" "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

// 회원가입 폼
const SignUp = () => {
  /*
  자 여기서 이제 zustand에 정의된 변수, 메서드를 가져왔어
  보통 여러개를 가져올테니 이 방법으로 통일하면 좋을듯 ?
  */
  const { isDuplicatedEmail, duplicateCheckEmail } = useBoundStore((state) => ({
    isDuplicatedEmail: state.isDuplicatedEmail,
    duplicateCheckEmail: state.duplicateCheckEmail,
  }));

  const theme = useTheme();

  const [userData, setUserData] = useState({
    userId: "",
    password: "",
    userName: "",
    userPhoneNumber: "",
  });

  const [emailError, setEmailError] = useState("");
  const [emailChecked, setEmailChecked] = useState(false);

  /*
  이메일 중복확인 버튼에 연결할 함수인데
  클릭이 되면 userData.userId 값을 아까 email 인자로 실어서 authSlice -> auth 순으로 쭉쭉 타고 가서 요청을 보내고 response를 받아올거임
  그래서 반환으로 중복인지 아닌지 true, false로 올거고 그 조건에 따라 보여줄 문구를 다르게 한 거애
  */
  const handleEmailCheck = async () => {
    try {
      await duplicateCheckEmail(userData.userId);
      setEmailChecked(true);
      if (isDuplicatedEmail) {
        setEmailError("이미 사용 중인 이메일입니다.");
      } else {
        setEmailError("사용 가능한 이메일입니다.");
      }
    } catch (error) {
      console.error("Email check error:", error);
      setEmailError("이메일 확인 중 오류가 발생했습니다.");
    }
  };

  /*
  이렇게 구현하면 이 함수 하나로 모든 객체의 input data를 관리할 수 있는데
  userData는 여기에 입력되는 회원정보인데 이건 전역으로 관리할게 아니자나? 그냥 여기서 모아서 요청 보내면 끝이니까
  그래서 useState로 여기서 관리될거고 set 안에 있는 내용은 ...userData는 스프레드 문법이라고 기존의 userDate를 복사해서 담는다고 보면대
  그 다음에 , [event.target.name]: event.target.value 는 이제 입력 받는 데이터를 추가로 넣어주는 건데 
  event.target.name 은 그 input 우린 TextFiled 썻는데 암튼 그 name 속성의 값이고 value는 input에 입력된 값이겠지
  이렇게 쓰게 되면 결국 기존 데이터 + 새로운 데이터인데 중복되는 키값에 새로운 값은 덮어쓰기되서 결국 갱신처리가 되벌임
  안에 조건문은 그냥 상황처리라 이해안되면 넘어가두대고
  */
  const handleChange = (event) => {
    setUserData({ ...userData, [event.target.name]: event.target.value });
    if (event.target.name === "userId") {
      setEmailChecked(false);
      setEmailError("");
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    console.log({
      userId: data.get("userId"),
      password: data.get("password"),
      userName: data.get("userName"),
      userPhoneNumber: data.get("userPhoneNumber"),
    });
  };

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <Box
        sx={{
          marginTop: 8,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <></>
        <Box component="form" noValidate onSubmit={handleSubmit}>
          <Grid container spacing={4}>
            <Grid item xs={12}>
              <TextField
                label="아이디"
                name="userId" // 이게 event.target.name이랑 엮이고
                id="userId"
                required
                fullWidth
                autoFocus
                value={userData.userId}
                onChange={handleChange}
              />
              <Box sx={{ display: "flex", justifyContent: "space-between" }}>
                <Button
                  variant="text"
                  onClick={handleEmailCheck} // 이걸 클릭하면 타고타고 올라가 결국 api 요청이 수행되고
                  disabled={!userData.userId}
                  sx={{ mt: 1 }}
                >
                  이메일 중복 확인
                </Button>
                {/* 아이디 입력 란 하단에 위치한 안내 문구 */}
                {/* userId가 입력이 되었고 이메일 중복 확인을 했다면 문구 출력 */}
                {userData.userId && emailChecked && (
                  <Typography
                    name="idNotice"
                    style={{
                      fontSize: theme.fontSize.small,
                      marginTop: 16,
                      color: isDuplicatedEmail ? "red" : "green",
                    }}
                  >
                    {emailError}
                  </Typography>
                )}
              </Box>
            </Grid>
            <Grid item xs={12}>
              <TextField
                label="비밀번호"
                name="password"
                type="password"
                id="password"
                required
                fullWidth
                value={userData.password}
                onChange={handleChange}
              />
              {/* 비밀번호 입력 란 하단에 위치한 안내 문구 */}
              <Typography
                name="pwNotice"
                style={{
                  fontSize: theme.fontSize.small,
                  marginTop: 10,
                  marginLeft: 10,
                }}
              >
                9~16자리 영문 대소문자, 숫자, 특수문자 조합
              </Typography>
            </Grid>
            <Grid item xs={12}>
              <TextField
                label="이름"
                id="userName"
                name="userName"
                required
                fullWidth
                value={userData.userName}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                label="휴대폰번호"
                id="userPhoneNumber"
                name="userPhoneNumber"
                required
                fullWidth
                value={userData.userPhoneNumber}
                onChange={handleChange}
              />
            </Grid>
          </Grid>
          <></>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 4, mb: 2, height: "50px" }}
            style={{ backgroundColor: theme.palette.primary.main }}
            disabled={isDuplicatedEmail || !emailChecked} // 이메일 중복 확인 후에만 회원가입 가능
          >
            회원가입 하기
          </Button>
        </Box>
      </Box>
      {/* <Copyright sx={{ mt: 5 }} /> */}
    </Container>
  );
};

export default SignUp;
