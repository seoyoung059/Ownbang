import Axios from "./index";

export const postRealEstate = (estateData) => {
  const response = Axios.post(`/rooms/agents`, estateData, {
    "Content-Type": "multipart/form-data",
  });
  return response;
};

export const getAllRealEstate = () => {
  const response = Axios.get("/rooms/agents");
  return response;
};
