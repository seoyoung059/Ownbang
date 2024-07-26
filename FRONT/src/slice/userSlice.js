import { getUserInfo } from "../api/user";

export const createUserSlice = (set) => ({
  user: { isAgent: false },
  fetchUser: async (token) => {
    try {
      const userData = await getUserInfo(token);
      set({ user: userData });
    } catch (error) {
      console.error(error);
      set({ user: null });
    }
  },
  modifyUser: async (token, data) => {
    try {
      const response = await patchUserInfo(token, data);
      console.log(response.result);
    } catch (error) {
      console.error(error);
    }
  },
  clearUser: () => set({ user: null }),
});
