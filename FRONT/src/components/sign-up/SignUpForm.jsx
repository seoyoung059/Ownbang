import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
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
  const {
    isDuplicatedEmail,
    duplicateCheckEmail,
    isDuplicatedPhoneNumber,
    duplicateCheckPhoneNumber,
    makeUser,
  } = useBoundStore((state) => ({
    isDuplicatedEmail: state.isDuplicatedEmail,
    duplicateCheckEmail: state.duplicateCheckEmail,
    isDuplicatedPhoneNumber: state.isDuplicatedPhoneNumber,
    duplicateCheckPhoneNumber: state.duplicateCheckPhoneNumber,
    makeUser: state.makeUser,
  }));

  const theme = useTheme();
  const navigate = useNavigate();

  const [userData, setUserData] = useState({
    userId: "",
    nickName: "",
    password: "",
    userName: "",
    userPhoneNumber: "",
  });

  const [emailError, setEmailError] = useState("");
  const [emailChecked, setEmailChecked] = useState(false);

  const [phoneNumberError, setPhoneNumberError] = useState("");
  const [phoneNumberChecked, setPhoneNumberChecked] = useState(false);

  // 이메일 중복 확인
  const handleEmailCheck = async () => {
    try {
      await duplicateCheckEmail(userData.userId);
      setEmailChecked(true);
    } catch (error) {
      console.error("Email check error:", error);
      setEmailError("이메일 확인 중 오류가 발생했습니다.");
    }
  };

  // 전화번호 중복 확인
  const handlePhoneNumberCheck = async () => {
    try {
      const phoneNumber = userData.userPhoneNumber.split("-").join("");
      await duplicateCheckPhoneNumber(phoneNumber);
      setPhoneNumberChecked(true);
    } catch (error) {
      console.error("PhoneNumber check error:", error);
      setPhoneNumberError("전화번호 확인 중 오류가 발생했습니다.");
    }
  };

  // 중복 확인 변경 감지
  useEffect(() => {
    if (emailChecked) {
      if (isDuplicatedEmail) {
        setEmailError("이미 사용 중인 이메일입니다.");
      } else {
        setEmailError("사용 가능한 이메일입니다.");
      }
    }
  }, [emailChecked, isDuplicatedEmail]);

  useEffect(() => {
    if (phoneNumberChecked) {
      if (isDuplicatedPhoneNumber) {
        setPhoneNumberError("이미 사용 중인 전화번호입니다.");
      } else {
        setPhoneNumberError("사용 가능한 전화번호입니다.");
      }
    }
  }, [phoneNumberChecked, isDuplicatedPhoneNumber]);

  const handleChange = (event) => {
    setUserData({ ...userData, [event.target.name]: event.target.value });
    if (event.target.name === "userId") {
      setEmailChecked(false);
      setEmailError("");
    }
    if (event.target.name === "userPhoneNumber") {
      setPhoneNumberChecked(false);
      setPhoneNumberError("");
    }
  };

  const handleCallNumberChange = (event) => {
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

    setUserData((prevUserData) => ({
      ...prevUserData,
      userPhoneNumber: formattedPhoneNumber,
    }));
  };

  // 회원가입 성공 시 TOAST 에러 처리 추가해야댐
  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    makeUser({
      email: data.get("userId"),
      password: data.get("password"),
      name: data.get("userName"),
      nickname: data.get("nickName"),
      phoneNumber: data.get("userPhoneNumber").split("-").join(""),
    }).then((res) => {
      if (res === "SUCCESS") {
        toast.success("회원가입에 성공했습니다.", {
          position: "bottom-left",
          autoClose: 2000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "light",
        });
        navigate("/login");
      }
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
                name="userId"
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
                  onClick={handleEmailCheck}
                  disabled={!userData.userId}
                  sx={{ mt: 1 }}
                >
                  이메일 중복 확인
                </Button>
                {userData.userId && emailChecked && (
                  <Typography
                    name="idNotice"
                    style={{
                      fontSize: theme.fontSize.small,
                      marginTop: 16,
                      color:
                        emailError === "이미 사용 중인 이메일입니다."
                          ? "red"
                          : "green",
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
              <Typography
                name="pwNotice"
                style={{
                  fontSize: theme.typography.body2.fontSize,
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
                label="닉네임"
                id="nickName"
                name="nickName"
                required
                fullWidth
                value={userData.nickName}
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
                onChange={handleCallNumberChange}
              />
              <Box sx={{ display: "flex", justifyContent: "space-between" }}>
                <Button
                  variant="text"
                  onClick={handlePhoneNumberCheck}
                  disabled={!userData.userPhoneNumber}
                  sx={{ mt: 1 }}
                >
                  전화번호 중복 확인
                </Button>
                {userData.userPhoneNumber && phoneNumberChecked && (
                  <Typography
                    name="phoneNotice"
                    style={{
                      fontSize: theme.fontSize.small,
                      marginTop: 16,
                      color:
                        phoneNumberError === "이미 사용 중인 전화번호입니다."
                          ? "red"
                          : "green",
                    }}
                  >
                    {phoneNumberError}
                  </Typography>
                )}
              </Box>
            </Grid>
          </Grid>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 4, mb: 2, height: "50px" }}
            style={{ backgroundColor: theme.palette.primary.main }}
            disabled={
              emailError === "이미 사용 중인 이메일입니다." ||
              !emailChecked ||
              phoneNumberError === "이미 사용 중인 전화번호입니다." ||
              !phoneNumberChecked
            }
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
