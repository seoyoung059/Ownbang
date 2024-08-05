import { checkEmail, checkPhoneNumber, signUp, login } from "../api/auth";
import { Cookies } from "react-cookie";
import { toast } from "react-toastify";

const cookies = new Cookies();

export const createAuthSlice = (set) => ({
  isAuthenticated: false,
  isDuplicatedEmail: false,
  isDuplicatedPhoneNumber: false,

  duplicateCheckEmail: async (email) => {
    const result = await checkEmail(email);
    set({ isDuplicatedEmail: result.data.isDuplicate });
  },

  duplicateCheckPhoneNumber: async (phoneNumber) => {
    const result = await checkPhoneNumber(phoneNumber);
    set({ isDuplicatedPhoneNumber: result.data.isDuplicate });
  },

  makeUser: async (userData) => {
    const result = await signUp(userData);
    return result.resultCode;
  },

  loginUser: async (loginData) => {
    const result = await login(loginData);
    localStorage.setItem("accessToken", result.data.accessToken);
    cookies.set("refreshToken", result.data.refreshToken);
    set({ isAuthenticated: true });
    return result;
  },

  isLogin: () => {
    return localStorage.getItem("accessToken");
  },

  logout: () => {
    localStorage.removeItem("accessToken");
    set({ isAuthenticated: false });
    toast.success("로그아웃 완료", {
      position: "bottom-left",
      autoClose: 2000,
      hideProgressBar: true,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: "light",
    });
  },
});
