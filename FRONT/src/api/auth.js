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

// 비밀번호 확인 API 요청
export const checkPassword = async (password) => {
  const response = await Axios.post("/auths/password-check", {
    password: password,
  });
  return response.data;
};

// 비밀번호 변경 요청
export const changePassword = async (password) => {
  const response = await Axios.patch("/auths/password-change", {
    password: password,
  });
  return response.data;
};

// 중개인 회원 전환 신청 API 요청
export const toAgent = async (agentData) => {
  const response = await Axios.post("/agents/auths/sign-up", agentData);
  return response.data;
};
