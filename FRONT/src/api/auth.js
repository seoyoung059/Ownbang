import Axios from "./index";

// 이메일 중복 확인 API 요청
export const checkEmail = async (email) => {
  const response = await Axios.get(`/auths/duplicates/email?email=${email}`);
  return response.data;
};

// 전화번호 중복 확인 API 요청
export const checkPhoneNumber = async (phoneNumber) => {
  const response = await Axios.get(
    `/auths/duplicates/phone?phoneNumber=${phoneNumber}`
  );
  return response.data;
};

// 회원가입 API 요청
export const signUp = async (userData) => {
  const response = await Axios.post("/auths/sign-up", userData);
  return response.data;
};

// 로그인 API 요청
export const login = async (loginData) => {
  const response = await Axios.post("/auths/login", loginData);
  return response.data;
};
