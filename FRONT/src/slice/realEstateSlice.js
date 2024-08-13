import { getRealEstateAll, getRealEstateDetail } from "../api/realestate";

export const createRealEstateSlice = (set) => ({
  room: {},

  realEstateData:[],

  getAllRoom: async (lat, lon) => {
    const result = await getRealEstateAll(lat, lon);
    // console.log("getRealEstateAll", result)
    set({ realEstateData: result.data});
    return result.data;
  },
  getRoom: async (roomNum) => {
    const result = await getRealEstateDetail(roomNum);
    // console.log(result);
    set({ room: result.data });
  },
});
