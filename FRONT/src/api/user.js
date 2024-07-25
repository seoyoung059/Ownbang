// src/api/userInfo.js

import { myInfoAxiosInstance } from "./index";

// 유저 데이터 조회
export const getUserInfo = async (token) => {
  const response = await myInfoAxiosInstance.get("", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.data.result;
};

// 유저 데이터 수정
export const patchUserInfo = async (token, data) => {
  const response = await myInfoAxiosInstance.patch("", data, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.data.result;
};
