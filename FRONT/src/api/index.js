import axios from "axios";

// axios 구조화
export const authsAxiosInstance = axios.create({
  baseURL: VITE_API_URL + "/api/auths",
  timeout: 10000, // 요청 타임아웃
  headers: {
    "Content-Type": "application/json",
    // 기타 필요한 헤더 설정
  },
});

export const myInfoAxiosInstance = axios.create({
  baseURL: VITE_API_URL + "/api/mypage",
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
    // 기타 필요한 헤더 설정
  },
});
