import { getToken, removeToken, getVideo } from "../api/video";

export const createVideoSlice = (set) => ({
  enterVideoRoom: async (id) => {
    const result = await getToken(id);
    console.log(result);
    return result.data;
  },
  leaveVideoRoom: async (id, token) => {
    const result = await removeToken(id, token);
    return result.data;
  },
  enterReplayRoom: async (id) => {
    const result = await getVideo(id);
    console.log(result.data);
    return result.data;
  },
});
