import { reservation } from "../api/reservation";

export const createReservation = (set) => ({
  makeReservation: async (reservationData) => {
    try {
      const result = await reservation(reservationData);
      console.log(result);
      return result;
    } catch (error) {
      console.error("Error making reservation:", error);
      throw error; // 에러를 다시 던져서 호출 측에서 처리할 수 있도록 합니다.
    }
  },
});
