import axios from "axios";
import { getCookie } from "react-cookie";

// axios instance

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL + "/api",
  timeout: 3000,
});

// 쿠키를 가져와서 있다면 헤더에 넣음
axiosInstance.interceptors.request.use((config) => {
  const accessToken = getCookie("accessToken");
  if (accessToken) {
    config.headers["Authorization"] = "Bearer" + accessToken;
    config.withCredentials = true;
  }
  return config;
});

const Axios = () => {
  return axiosInstance;
};

export default Axios;
