import { getUserInfo } from "../api/user";

export const createUserSlice = (set) => ({
  user: null,
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
      const updatedUser = await patchUserInfo(token, data);
      set({ user: updatedUser });
    } catch (error) {
      console.error(error);
    }
  },
  clearUser: () => set({ user: null }),
});
