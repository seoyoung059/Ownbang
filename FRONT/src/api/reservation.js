import Axios from "./index";

// 예약하기 API 요청
export const reservation = async (reservationData) => {
  const response = await Axios.post("/reservations/", reservationData);
  return response.data;
};

// 예약 조회 API 요청
export const myReservationList = async () => {
  const response = await Axios.get("/reservations/list");
  return response.data;
};
