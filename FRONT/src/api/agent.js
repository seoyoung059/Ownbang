import Axios from "./index";

export const postRealEstate = (estateData) => {
  const response = Axios.post(`/rooms/agents`, estateData, {
    "Content-Type": "multipart/form-data",
  });
  return response;
};
