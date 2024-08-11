import React from "react";
import styled, { keyframes } from "styled-components";
import { Box, Typography, useTheme } from "@mui/material";

const loadingWaveAnimation = keyframes`
  0% {
    height: 10px;
  }
  50% {
    height: 50px;
  }
  100% {
    height: 10px;
  }
`;

const LoadingWave = styled.div`
  width: 300px;
  height: 100px;
  display: flex;
  justify-content: center;
  align-items: flex-end;
`;

const LoadingBar = styled.div`
  width: 40px;
  height: 10px;
  margin: 0 5px;
  background-color: #395886;
  border-radius: 5px;
  animation: ${loadingWaveAnimation} 1s ease-in-out infinite;
  &:nth-child(2) {
    animation-delay: 0.1s;
  }
  &:nth-child(3) {
    animation-delay: 0.2s;
  }
  &:nth-child(4) {
    animation-delay: 0.3s;
  }
`;

export default function Loading({ message }) {
  const theme = useTheme();

  return (
    <>
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          mt: 24,
          alignItems: "center",
        }}
      >
        <LoadingWave>
          <LoadingBar />
          <LoadingBar />
          <LoadingBar />
          <LoadingBar />
        </LoadingWave>
        <Typography
          sx={{
            mt: 1,
            color: theme.palette.text.secondary,
            fontWeight: theme.fontWeight.bold,
          }}
        >
          {message}
        </Typography>
      </Box>
    </>
  );
}
