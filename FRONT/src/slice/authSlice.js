import {
  checkEmail,
  checkPhoneNumber,
  signUp,
  login,
  checkPassword,
  toAgent,
  changePassword,
} from "../api/auth";
import { Cookies } from "react-cookie";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const cookies = new Cookies();

export const createAuthSlice = (set) => ({
  isAuthenticated: !!localStorage.getItem("accessToken"),
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
    // toast.success("로그아웃 완료", {
    //   position: "bottom-left",
    //   autoClose: 2000,
    //   hideProgressBar: true,
    //   closeOnClick: true,
    //   pauseOnHover: true,
    //   draggable: true,
    //   progress: undefined,
    //   theme: "light",
    // });
  },

  confirmPassword: async (password) => {
    const result = await checkPassword(password);
    return result.data.isCorrect;
  },

  newPassword: async (password) => {
    const result = await changePassword(password);
    return result;
  },

  upgradeAgent: async (agentData) => {
    const result = await toAgent(agentData);
    return result;
  },
});
