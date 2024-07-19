import React, { useState } from "react";
import { Grid, TextField, Typography } from "@mui/material";
import { useTheme } from "@emotion/react";
export default function PasswordChangeForm() {
  const theme = useTheme();
  const [password, setPassword] = useState("");
  const [passwordCheck, setPasswordCheck] = useState("");

  const handlePasswordChange = (e) => {
    const value = e.target.value;
    setPassword(() => value);
  };
  const [passwordError, setPasswordError] = useState(false);
  return (
    <>
      <Grid item xs={12}>
        <TextField
          label="비밀번호 확인"
          name="passwordConfirm"
          type="password"
          id="passwordConfirm"
          fullWidth
        />
        {passwordError && (
          <Typography
            variant="body2"
            color="error"
            sx={{
              mt: 1,
              textAlign: "end",
              fontSize: theme.typography.fontSize.small,
            }}
          >
            비밀번호가 일치하지 않습니다.
          </Typography>
        )}
      </Grid>
    </>
  );
}
