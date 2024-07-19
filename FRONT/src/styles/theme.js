import { createTheme } from "@mui/material";

const theme = createTheme({
  fontSize: {
    small: "10px",
    large: "18px",
    larger: "20px",
  },

  fontWeight: {
    medium: 500,
    bold: 700,
    heavy: 900,
  },

  // 텍스트 관련 속성 정의
  typography: {
    fontSize: 14,
    fontWeight: 400,
    fontFamily: "Roboto, sans-serif",
    // lineHeight
    // letterSpacing
  },

  // 색상 관련 스타일 정의
  palette: {
    // 주 테마 색상 정의
    primary: {
      main: "#638ECB",
      dark: "#395886", // 진한 버전
      light: "#8aaee0", // 밝은 버전
    },
    // 보조 테마 색상 정의
    secondary: {
      main: "#d5deef",
      dark: "#b1c9ef", // 진한 버전
      light: "#f0f3fa", // 밝은 버전
    },
    // 오류 색상 정의
    error: {
      main: "#f44336",
    },
    // 경고 색상 정의
    warning: {
      main: "#ff9800",
    },
    // 정보 색상 정의
    info: {
      main: "#2196f3",
    },
    // 성공 색상 정의
    success: {
      main: "#4caf50",
    },
    // 텍스트 색상 정의
    text: {
      primary: "rgba(0, 0, 0, 0.87)",
      secondary: "rgba(0, 0, 0, 0.54)",
      disabled: "rgba(0, 0, 0, 0.38)",
    },
    // 구분선 색상 정의
    divider: "rgba(0, 0, 0, 0.12)",
    // 배경 색상 정의
    background: {
      default: "#ffffff",
      paper: "#f5f5f5", // 종이나 패널
    },
    // 작업 요소 색상 정의
    action: {
      active: "#3f51b5", // 활성화된 요소
      hover: "#f5f5f5", // 마우스 호버 시
      selected: "#e0e0e0", // 선택된 요소
      disabled: "#9e9e9e", // 비활성화된 요소
      disabledBackground: "#e0e0e0", // 비활성화된 요소의 배경색
    },
    // 공통 색상 정의
    common: {
      black: "#000000",
      white: "#ffffff",
      grey: "#797979",
    },
  },
});

export default theme;
