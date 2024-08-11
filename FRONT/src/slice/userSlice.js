import { getUserInfo, patchUserInfo } from "../api/user";

export const createUserSlice = (set) => ({
  user: {},
  fetchUser: async (token) => {
    try {
      const response = await getUserInfo(token);
      set({ user: response.data });
    } catch (error) {
      set({ user: {} });
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
