import axios from "axios";

// axios 요청 구조화
const axiosInstance = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  timeout: 10000, // 요청 타임아웃 설정
  headers: {
    "Content-Type": "application/json",
    // 기타 필요한 헤더 설정
  },
});

export default axiosInstance;
