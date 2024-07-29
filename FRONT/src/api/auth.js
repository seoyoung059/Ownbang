import Axios from "./index";

// api 명세 보면 대분류(prefix) 기준으로 나눠져 있으니 이 기준으로 요청 파일 구분할 예정

export const login = async (email, password) => {
  const response = await Axios.post("/auth/login", {
    email,
    password,
  });
  return response.data;
};

export const logout = async () => {
  const response = await Axios.post("/auth/logout");
  return response.data;
};
