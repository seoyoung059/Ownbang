import { setCookie, getCookie, removeCookie } from "react-cookie";

import { login, logout } from "../api/auth";

// 일단 이 정도로 구성해봤는데 통신하면서 수정-보완해야 할듯

export const authSlice = (set) => ({
  isLogin: true,
  email: "",
  password: "",
  doLogin: async () => {
    const jwt = await login(email, password);
    await setCookie("jwt", jwt, { path: "/" });
    await set({ isLogin: true });
  },
  doLogout: async () => {
    await logout();
    await removeCookie("jwt", { path: "/" });
    await set({ isLogin: false });
  },
  checkLogin: () => {
    const jwt = getCookie("jwt");
    if (jwt) {
      set({ isLogin: true });
    } else {
      set({ isLogin: false });
    }
  },
});
