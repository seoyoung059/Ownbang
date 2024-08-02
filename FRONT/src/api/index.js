import axios from "axios";
import { Cookies } from "react-cookie";

// 쿠키 인스턴스 생성
const cookies = new Cookies();

/*
axios instance 생성 - URL 연결과 헤더에 실을 토큰을 통일 시킬 수 있음
URL 연결 : /api 까지 연결해둠
로그인을 수행하면 accessToken과 그 토큰을 관리할 refreshToken이 오는데 이걸 저장할거야
accessToken 로컬 스토리지에 저장할 거고 refreshToken은 쿠키에 저장할건데 기본적인 통신은 accessToken을 쓰니까
일단 기본값으로 그 토큰을 헤더에 넣어놓을게 refreshToken 관련 로직은 내가 나중에 추가할게 해당 없는 기능만 우선 구현하면 될듯
*/
const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL + "/api",
  timeout: 3000,
});

axiosInstance.interceptors.request.use(
  (config) => {
    // const accessToken = cookies.get("accessToken");
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken) {
      config.headers["Authorization"] = "Bearer " + accessToken;
      config.withCredentials = true;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// default로 export
export default axiosInstance;
