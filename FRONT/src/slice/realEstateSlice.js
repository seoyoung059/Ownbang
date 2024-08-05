import { getRealEstateDetail } from "../api/realestate";

export const createRealEstateSlice = (set) => ({
  room: {},

  realEstateData: {
    data: [
      {
        id: 1,
        latitude: 37.5,
        longitude: 127.039,
        dealType: "월세",
        roomType: "오피스텔",
        structure: "원룸",
        isLoft: false,
        exclusiveArea: 12.66,
        supplyArea: 18.33,
        roomFloor: 3,
        deposit: 20000000,
        monthlyRent: 670000,
        maintenanceFee: 99999,
        parcel: "변경된지번주소",
        profileImageUrl: "changedURL",
      },
      {
        id: 2,
        latitude: 37.561022215939886,
        longitude: 126.9811778682524,
        dealType: "매매",
        roomType: "상가",
        structure: "단일 구조",
        isLoft: false,
        exclusiveArea: 100.0,
        supplyArea: 150.0,
        roomFloor: 1,
        deposit: 500000000,
        monthlyRent: 500000,
        maintenanceFee: 1000000,
        parcel: "서울시 중구",
        profileImageUrl: "신세계백화점 본점",
      },
    ],
  },

  getRoom: async (roomNum) => {
    const result = await getRealEstateDetail(roomNum);
    console.log(result);
    set({ room: result.data });
  },
});
