import Axios from "./index";

export const getToken = async (id) => {
  const response = await Axios.post("/webrtcs/get-token", {
    reservationId: id,
  });
  return response.data;
};

export const removeToken = async (id, token) => {
  const response = await Axios.post("/webrtcs/remove-token", {
    reservationId: id,
    token: token,
  });
  return response.data;
};
