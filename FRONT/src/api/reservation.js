import Axios from "./index";

// 예약하기 API 요청
export const reservation = async (reservationData) => {
  const response = await Axios.post("/reservations/", reservationData);
  return response.data;
};
