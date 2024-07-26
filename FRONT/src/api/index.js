import axios from "axios";

// axios instance
// 경로 수정 시 여기서만 바꿔주면 되고 env에 VITE_API_URL로 요청 보낼 서버 주소 넣기

// interceptor 추가 고려

export const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL + "/api",
  timeout: 10000, // 요청 타임아웃
  headers: {
    "Content-Type": "application/json",
    // 기타 필요한 헤더 설정
  },
});
