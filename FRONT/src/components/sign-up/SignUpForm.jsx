import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

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
    passwordConfirm: "",
    userName: "",
    userPhoneNumber: "",
  });

  const [emailError, setEmailError] = useState("");
  const [emailChecked, setEmailChecked] = useState(false);
  const [emailValidError, setEmailValidError] = useState("");

  const [phoneNumberError, setPhoneNumberError] = useState("");
  const [phoneNumberChecked, setPhoneNumberChecked] = useState(false);

  const [passwordError, setPasswordError] = useState("");
  const [passwordTouched, setPasswordTouched] = useState(false);
  const [passwordConfirmError, setPasswordConfirmError] = useState("");
  const [passwordConfirmTouched, setPasswordConfirmTouched] = useState(false);

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  const passwordRegex =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{9,16}$/;

  const handleEmailCheck = async () => {
    if (!emailRegex.test(userData.userId)) {
      setEmailValidError("유효한 이메일 주소를 입력해주세요.");
      return;
    }
    setEmailValidError(""); // Clear any existing error message
    try {
      await duplicateCheckEmail(userData.userId);
      setEmailChecked(true);
    } catch (error) {
      console.error("Email check error:", error);
      setEmailError("이메일 확인 중 오류가 발생했습니다.");
    }
  };

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
      setEmailValidError("");
    }
    if (event.target.name === "userPhoneNumber") {
      setPhoneNumberChecked(false);
      setPhoneNumberError("");
    }
    if (event.target.name === "password") {
      setPasswordTouched(false);
      setPasswordError("");
      setPasswordConfirmError("");
    }
    if (event.target.name === "passwordConfirm") {
      setPasswordConfirmTouched(false);
      setPasswordConfirmError("");
    }
  };

  const handlePasswordBlur = (event) => {
    const password = event.target.value;
    setPasswordTouched(true);
    if (password === "") {
      setPasswordError("");
    } else if (!passwordRegex.test(password)) {
      setPasswordError("유효한 비밀번호가 아닙니다.");
    } else {
      setPasswordError("");
    }
  };

  const handlePasswordConfirmBlur = (event) => {
    const passwordConfirm = event.target.value;
    setPasswordConfirmTouched(true);
    if (passwordConfirm !== userData.password) {
      setPasswordConfirmError("비밀번호가 일치하지 않습니다.");
    } else {
      setPasswordConfirmError("");
    }
  };

  const handleEmailBlur = (event) => {
    const email = event.target.value;
    if (email === "") {
      setEmailValidError(""); // Clear error if email is empty
    } else if (!emailRegex.test(email)) {
      setEmailValidError("유효한 이메일 주소를 입력해주세요.");
    } else {
      setEmailValidError(""); // Clear error if email is valid
    }
  };

  const handleCallNumberChange = (event) => {
    let formattedPhoneNumber = event.target.value.replace(/[^\d]/g, "");

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
      profileImageUrl: `https://api.multiavatar.com/${
        Math.floor(Math.random() * 1000) + 1
      }`,
    }));
  };

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
        toast.success("회원가입 완료", {
          position: "bottom-left",
          autoClose: 2000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "light",
        });
      }
      navigate("/login");
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
                label="이메일"
                name="userId"
                id="userId"
                required
                fullWidth
                autoFocus
                value={userData.userId}
                onChange={handleChange}
                onBlur={handleEmailBlur}
                error={emailValidError !== ""}
                helperText={emailValidError || " "}
                FormHelperTextProps={{
                  style: {
                    fontSize: theme.fontSize.small,
                    color: emailValidError ? "red" : "inherit",
                  },
                }}
              />
              <Box sx={{ display: "flex", justifyContent: "space-between" }}>
                <Button
                  variant="text"
                  onClick={handleEmailCheck}
                  disabled={!userData.userId || emailValidError !== ""}
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
                onBlur={handlePasswordBlur}
                error={passwordTouched && passwordError !== ""}
                helperText={
                  passwordTouched
                    ? passwordError
                      ? "유효한 비밀번호가 아닙니다.(9~16자리 영문 대소문자, 숫자, 특수문자)"
                      : userData.password === ""
                      ? "9~16자리 영문 대소문자, 숫자, 특수문자 조합"
                      : "유효한 비밀번호입니다."
                    : "9~16자리 영문 대소문자, 숫자, 특수문자 조합"
                }
                FormHelperTextProps={{
                  style: {
                    fontSize: theme.fontSize.small,
                    color:
                      passwordTouched &&
                      !passwordError &&
                      userData.password !== ""
                        ? "green"
                        : passwordTouched && passwordError
                        ? "red"
                        : "inherit",
                  },
                }}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                label="비밀번호 확인"
                name="passwordConfirm"
                type="password"
                id="passwordConfirm"
                required
                fullWidth
                value={userData.passwordConfirm}
                onChange={handleChange}
                onBlur={handlePasswordConfirmBlur}
                error={
                  passwordConfirmTouched &&
                  userData.passwordConfirm !== "" &&
                  passwordConfirmError !== ""
                }
                helperText={
                  passwordConfirmTouched
                    ? userData.passwordConfirm === ""
                      ? " "
                      : passwordConfirmError !== ""
                      ? "비밀번호가 일치하지 않습니다."
                      : "비밀번호가 일치합니다."
                    : " "
                }
                FormHelperTextProps={{
                  style: {
                    fontSize: theme.fontSize.small,
                    color:
                      passwordConfirmTouched &&
                      userData.passwordConfirm !== "" &&
                      passwordConfirmError !== ""
                        ? "red"
                        : passwordConfirmTouched && passwordConfirmError === ""
                        ? "green"
                        : "inherit",
                  },
                }}
              />
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
              !phoneNumberChecked ||
              passwordError !== "" ||
              userData.password === "" ||
              passwordConfirmError !== "" ||
              userData.passwordConfirm === ""
            }
          >
            회원가입 하기
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default SignUp;
