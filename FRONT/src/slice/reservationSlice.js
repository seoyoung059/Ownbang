import { reservation, reservationList } from "../api/reservation";

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

  reservationAll: [],
  getReservationList: async () => {
    try {
      const response = await reservationList();
      set({ reservationAll: response.data });
      console.log(response.data);
    } catch (error) {
      console.error("Error fetching reservation list:", error);
    }
  },
});
