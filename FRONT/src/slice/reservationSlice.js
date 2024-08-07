import { reservation } from "../api/reservation";

export const createReservation = (set) => ({
  makeReservation: async (reservationData) => {
    try {
      const result = await reservation(reservationData);
      console.log(result);
      return result;
    } catch (error) {
      console.error("Error making reservation:", error);
      throw error;
    }
  },
});
