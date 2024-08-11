import axios from "axios";
import { Cookies } from "react-cookie";

const cookies = new Cookies();

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL + "/api",
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
  (error) => Promise.reject(error)
);

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response && error.response.status === 401) {
      const userConfirmed = window.confirm(
        "세션이 만료되었습니다. 세션을 연장하시겠습니까?"
      );

      if (userConfirmed) {
        try {
          const refreshToken = cookies.get("refreshToken");
          const accessToken = localStorage.getItem("accessToken");

          if (refreshToken && accessToken) {
            const { data } = await axios.post(
              `${import.meta.env.VITE_API_URL}/api/auths/refresh`,
              { accessToken, refreshToken }
            );

            localStorage.setItem("accessToken", data.data.accessToken);
            cookies.set("refreshToken", data.data.refreshToken, {
              path: "/",
            });

            originalRequest.headers["Authorization"] =
              "Bearer " + data.data.accessToken;

            return axiosInstance(originalRequest);
          }
        } catch (refreshError) {
          console.error("토큰 갱신 실패:", refreshError);
          localStorage.removeItem("accessToken");
          cookies.remove("refreshToken");
          window.location.href = "/login";
        }
      } else {
        localStorage.removeItem("accessToken");
        cookies.remove("refreshToken");
        window.location.href = "/login";
      }

      return new Promise(() => {}); // 대기 상태 유지
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
