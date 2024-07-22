import React, { useState } from "react";
import {
  Grid,
  TextField,
  Typography,
  Box,
  Divider,
  Button,
} from "@mui/material";
import { useTheme } from "@emotion/react";

export default function PasswordChangeForm({ toggleEdit }) {
  const theme = useTheme();
  const [password, setPassword] = useState("");
  const [passwordCheck, setPasswordCheck] = useState("");
  const [passwordError, setPasswordError] = useState(false);
  const [passwordFormatError, setPasswordFormatError] = useState(false);
  const [passwordTouched, setPasswordTouched] = useState(false);
  const [passwordCheckTouched, setPasswordCheckTouched] = useState(false);

  const passwordRegex =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{9,16}$/;

  const handlePasswordChange = (e) => {
    const value = e.target.value;
    setPassword(value);
    setPasswordTouched(false);
    setPasswordFormatError(false);
    setPasswordError(false);
  };

  const handlePasswordCheckChange = (e) => {
    const value = e.target.value;
    setPasswordCheck(value);
    setPasswordCheckTouched(false);
    setPasswordError(false);
  };

  const handlePasswordBlur = () => {
    setPasswordTouched(true);
    if (password === "") {
      setPasswordFormatError(false);
    } else {
      setPasswordFormatError(!passwordRegex.test(password));
    }
  };

  const handlePasswordCheckBlur = () => {
    setPasswordCheckTouched(true);
    if (passwordCheck === "") {
      setPasswordError(false);
    } else {
      setPasswordError(password !== passwordCheck);
    }
  };

  const handleSubmit = () => {
    if (!passwordFormatError && !passwordError && password && passwordCheck) {
      console.log("새 비밀번호:", password);
    } else {
      alert("비밀번호를 확인해주세요."); // toast로 대체
    }
  };

  return (
    <>
      <Box sx={{ mt: 18 }}>
        <Grid container spacing={2}>
          <Typography
            sx={{ mt: 12, pl: 2, fontWeight: theme.typography.fontWeightBold }}
            component="h1"
            variant="h5"
          >
            비밀번호 변경
          </Typography>
          <Divider component="div" sx={{ mt: 2, width: "100%" }} />
          <Grid item xs={12}>
            <TextField
              label="새 비밀번호"
              name="password"
              type="password"
              id="password"
              fullWidth
              value={password}
              onChange={handlePasswordChange}
              onBlur={handlePasswordBlur}
              error={passwordTouched && passwordFormatError}
              helperText={
                passwordTouched && passwordFormatError
                  ? "영문자, 숫자, 특수문자를 포함하여 9자 ~ 16자이어야 합니다."
                  : ""
              }
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              label="비밀번호 확인"
              name="passwordConfirm"
              type="password"
              id="passwordConfirm"
              fullWidth
              value={passwordCheck}
              onChange={handlePasswordCheckChange}
              onBlur={handlePasswordCheckBlur}
              error={passwordCheckTouched && passwordError}
              helperText={
                passwordCheckTouched && passwordError
                  ? "비밀번호가 일치하지 않습니다."
                  : ""
              }
            />
          </Grid>
          <Grid
            item
            xs={12}
            sx={{ display: "flex", justifyContent: "space-around" }}
          >
            <Button
              variant="contained"
              onClick={() => toggleEdit(false)}
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
        </Grid>
      </Box>
    </>
  );
}
