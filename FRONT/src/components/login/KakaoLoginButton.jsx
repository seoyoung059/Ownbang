import React from "react";

import { Button, Box, Typography, useTheme } from "@mui/material";

export default function KakaoLoginButton() {
  const theme = useTheme();
  return (
    <Button
      variant="contained"
      sx={{
        backgroundColor: "#fee500",
        color: "rgba(0, 0, 0, 0.85)",
        "&:hover": {
          bgcolor: "#fee500",
        },
        padding: "10px 20px",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        gap: 1,
        width: "100%",
        mb: 2,
      }}
    >
      <svg
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
        style={{ marginRight: "4px", marginBottom: "8px" }}
      >
        <path
          clip-rule="evenodd"
          d="M15 7C10.029 7 6 10.129 6 13.989C6 16.389 7.559 18.505 9.932 19.764L8.933 23.431C8.845 23.754 9.213 24.013 9.497 23.826L13.874 20.921C14.243 20.958 14.618 20.978 15 20.978C19.971 20.978 24 17.849 24 13.989C24 10.129 19.971 7 15 7Z"
          fill="black"
          fill-rule="evenodd"
        ></path>
      </svg>
      카카오 로그인
    </Button>
  );
}
