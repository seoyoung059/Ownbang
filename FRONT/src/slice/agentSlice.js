import { postRealEstate } from "../api/agent";

export const createAgentSlice = (set) => ({
  makeRoom: async (estateData) => {
    const result = await postRealEstate(estateData);
    console.log(result);
    return result;
  },
});
