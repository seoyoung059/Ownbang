import Axios from "./index";

// 유저 데이터 조회
export const getUserInfo = async () => {
  const response = await Axios.get("/mypage");
  return response.data;
};

// 유저 데이터 수정
export const patchUserInfo = async (data) => {
  const response = await Axios.patch("/mypage", data, {
    "Content-Type": "multipart/form-data",
  });
  return response.data;
};
