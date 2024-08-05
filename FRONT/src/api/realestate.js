import Axios from "./index";

// 매물 상세 정보 확인 API 요청
export const getRealEstateDetail = async (roomNum) => {
  const response = await Axios.get(`/rooms/${roomNum}`);
  return response.data;
};
