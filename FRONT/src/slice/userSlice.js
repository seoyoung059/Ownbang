import { getUserInfo, patchUserInfo } from "../api/user";

export const createUserSlice = (set) => ({
  user: null,
  fetchUser: async (token) => {
    try {
      const userData = await getUserInfo(token);
      set({ user: userData.data });
    } catch (error) {
      set({ user: null });
    }
  },
  modifyUser: async (data) => {
    try {
      const response = await patchUserInfo(data);
      console.log(response);
      return response;
    } catch (error) {
      console.error(error);
    }
  },
  clearUser: () => set({ user: null }),
});
