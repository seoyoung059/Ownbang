import { reservation } from "../api/reservation";

export const createReservation = (set) => ({
  makeReservation: async (reservationData) => {
    const result = await reservation(reservationData);
    console.log(result);
    return result;
  },
});
