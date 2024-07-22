import React, { useState } from "react";
import { Grid, TextField, Typography } from "@mui/material";
import { useTheme } from "@emotion/react";

export default function PasswordChangeForm() {
  const theme = useTheme();
  const [password, setPassword] = useState("");
  const [passwordCheck, setPasswordCheck] = useState("");
  const [passwordError, setPasswordError] = useState(false);
  const [passwordFormatError, setPasswordFormatError] = useState(false);

  const passwordRegex =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{9,16}$/;

  const handlePasswordChange = (e) => {
    const value = e.target.value;
    setPassword(value);
    setPasswordError(value !== passwordCheck);
    setPasswordFormatError(!passwordRegex.test(value));
  };

  const handlePasswordCheckChange = (e) => {
    const value = e.target.value;
    setPasswordCheck(value);
    setPasswordError(value !== password);
  };

  return (
    <>
      <Grid container spacing={2}>
        <Grid item xs={12}>
          <TextField
            label="새 비밀번호"
            name="password"
            type="password"
            id="password"
            fullWidth
            value={password}
            onChange={handlePasswordChange}
            error={passwordFormatError}
            helperText={
              passwordFormatError
                ? "비밀번호는 영문자, 숫자, 특수문자를 포함하여 최소 9자, 최대 16자이어야 합니다."
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
            error={passwordError}
            helperText={passwordError ? "비밀번호가 일치하지 않습니다." : ""}
          />
        </Grid>
      </Grid>
    </>
  );
}
