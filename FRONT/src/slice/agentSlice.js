import { postRealEstate, getAllRealEstate } from "../api/agent";

export const createAgentSlice = (set) => ({
  myRooms: [],
  makeRoom: async (estateData) => {
    const result = await postRealEstate(estateData);
    return result;
  },
  viewAllRooms: async () => {
    const result = await getAllRealEstate();
    console.log(result);
    set({ myRooms: result.data.data });
  },
});
