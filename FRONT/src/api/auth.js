import { authsAxiosInstance } from "./index";

export const login = async (email, password) => {
  const response = await authsAxiosInstance.post("/login", {
    email,
    password,
  });
  return response.data;
};

export const logout = async () => {
  const response = await authsAxiosInstance.post("/auth/logout");
  return response.data;
};
