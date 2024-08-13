import Axios from "./index";

// 매물 전체 정보 확인 API 요청
export const getRealEstateAll = async (lat, lon) => {
  const response = await Axios.get(`/rooms/search?lat=${lat}&lon=${lon}`);
  // console.log(response)
  return response.data;
};
// 매물 상세 정보 확인 API 요청
export const getRealEstateDetail = async (roomNum) => {
  const response = await Axios.get(`/rooms/search/${roomNum}`);
  return response.data;
};
