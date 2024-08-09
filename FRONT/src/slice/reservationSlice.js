import {
  reservation,
  myReservationList,
  agentReservationList,
  agentReservationConfirm,
  agentReservationDeny,
} from "../api/reservation";

export const createReservation = (set) => ({
  makeReservation: async (reservationData) => {
    try {
      const result = await reservation(reservationData);
      console.log(result);
      return result;
    } catch (error) {
      console.error("Error making reservation:", error);
      throw error; // 에러를 다시 던져서 호출 측에서 처리할 수 있도록
    }
  },

  reservations: [],
  getReservationList: async () => {
    try {
      const result = await myReservationList();
      console.log("reservation list:", result);
      set({
        reservations: Array.isArray(result.data.reservations)
          ? result.data.reservations
          : [],
      });
      return result;
    } catch (error) {
      console.error("Error getting reservation list:", error);
      throw error;
    }
  },

  agentReservations: [],
  getAgentReservationList: async () => {
    try {
      const result = await agentReservationList();
      console.log("agent reservation list:", result);
      set({
        agentReservations: Array.isArray(result.data.reservations)
          ? result.data.reservations
          : [],
      });
      return result;
    } catch (error) {
      console.error("Error getting reservation list:", error);
      throw error;
    }
  },

  confirmReservation: async (reservationId) => {
    try {
      const result = await agentReservationConfirm(reservationId);
      console.log(result);
      return result;
    } catch (error) {
      console.error("Error making reservation:", error);
      throw error;
    }
  },

  denyReservation: async (reservationId) => {
    try {
      const result = await agentReservationDeny(reservationId);
      console.log(result);
      return result;
    } catch (error) {
      console.error("Error denying reservation:", error);
      throw error;
    }
  },
});
