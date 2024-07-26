import { axiosInstance } from "./index";

// 유저 데이터 조회
export const getUserInfo = async (token) => {
  const response = await axiosInstance.get("/mypage", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.data.result;
};

// 유저 데이터 수정
export const patchUserInfo = async (token, data) => {
  const response = await axiosInstance.patch("/mypage", data, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.data.result;
};
