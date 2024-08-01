import Axios from "./index";

// 유저 데이터 조회
export const getUserInfo = async (token) => {
  const response = await Axios.get("/mypage", {});
  return response.data.result;
};

// 유저 데이터 수정
export const patchUserInfo = async (token, data) => {
  const response = await Axios.patch("/mypage", data, {});
  return response.data.result;
};
