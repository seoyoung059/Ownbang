import { getRealEstateAll, getRealEstateDetail } from "../api/realestate";

export const createRealEstateSlice = (set) => ({
  room: {},

  realEstateData: {
    data: [],
  },

  getAllRoom: async () => {
    const result = await getRealEstateAll();
    // console.log(result);
    set({ realEstateData: result });
  },
  getRoom: async (roomNum) => {
    const result = await getRealEstateDetail(roomNum);
    console.log(result);
    set({ room: result.data });
  },
});
