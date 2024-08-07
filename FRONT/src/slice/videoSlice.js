import { getToken, removeToken } from "../api/video";

export const createVideoSlice = (set) => ({
  enterVideoRoom: async (id) => {
    const result = await getToken(id);
    console.log(result);
    return result.data.token;
  },
  leaveVideoRoom: async (id, token) => {
    const result = await removeToken(id, token);
    return result.data;
  },
});
