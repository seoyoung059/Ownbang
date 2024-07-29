import axios from "axios";

// axios instance

export const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL + "/api",
  timeout: 10000, // 요청 타임아웃
  headers: {
    "Content-Type": "application/json",
    // 기타 필요한 헤더 설정
  },
});
