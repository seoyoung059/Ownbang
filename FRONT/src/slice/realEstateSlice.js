import { getRealEstateAll, getRealEstateDetail } from "../api/realestate";

export const createRealEstateSlice = (set) => ({
  room: {},
  loading: false,

  realEstateData: [],

  getAllRoom: async (lat, lon) => {
    const result = await getRealEstateAll(lat, lon);
    // console.log("getRealEstateAll", result)
    set({ realEstateData: result.data });
    return result.data;
  },
  getRoom: async (roomNum) => {
    set({ loading: true, room: {} }); // 로딩 시작 및 이전 데이터 초기화
    const result = await getRealEstateDetail(roomNum);
    set({ room: result.data, loading: false }); // 로딩 완료
  },
});
