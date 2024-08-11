import {
  postRealEstate,
  getAllRealEstate,
  getAgentInfo,
  patchAgentInfo,
  getAgentWorkhour,
  postAgentWorkhour,
  patchAgentWorkhour,
} from "../api/agent";

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
  getMyAgentInfo: async () => {
    const result = await getAgentInfo();
    console.log(result);
    return result.data.data;
  },
  modifyMyAgentInfo: async (data) => {
    const result = await patchAgentInfo(data);
    console.log(result);
    return result.data;
  },
  getMyAgentWorkhour: async () => {
    const result = await getAgentWorkhour();
    console.log(result);
    return result.data;
  },
  postMyAgentWorkhour: async (data) => {
    const result = await postAgentWorkhour(data);
    console.log(result);
    return result.data;
  },
  patchMyAgentWorkhour: async (data) => {
    const result = await patchAgentWorkhour(data);
    console.log(result);
    return result.data;
  },
});
