import axios from "axios";
import { Cookies } from "react-cookie";

// 쿠키 인스턴스 생성
const cookies = new Cookies();

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL + "/api",
  // timeout: 10000,
});

axiosInstance.interceptors.request.use(
  (config) => {
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
