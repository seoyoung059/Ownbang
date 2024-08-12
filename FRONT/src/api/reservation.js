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

// 중개인 예약 목록 조회 API 요청
export const agentReservationList = async () => {
  const response = await Axios.get("/agents/reservations");
  return response.data;
};

// 중개인 예약 확정 API 요청
export const agentReservationConfirm = async (reservationId) => {
  try {
    const response = await Axios.patch(
      `/agents/reservations/${reservationId}`,
      reservation.id
    );
    return response.data;
  } catch (error) {
    console.error("Error in agentReservationConfirm API:", error);
    throw error;
  }
};

// 중개인 예약 거절 API 요청
export const agentReservationDeny = async (reservationId) => {
  try {
    const response = await Axios.patch(
      `/agents/reservations/delete/${reservationId}`
    );
    return response.data;
  } catch (error) {
    console.error("Error in agentReservationDeny API:", error);
    throw error;
  }
};
