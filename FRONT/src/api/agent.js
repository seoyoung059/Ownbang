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

export const getAgentInfo = () => {
  const response = Axios.get("/agents/mypage");
  return response;
};

export const patchAgentInfo = (data) => {
  const response = Axios.patch("/agents/mypage", data);
  return response;
};

export const getAgentWorkhour = () => {
  const response = Axios.get("/agents/workhour");
  return response;
};

export const postAgentWorkhour = (data) => {
  const response = Axios.post("/agents/workhour", data);
  return response;
};

export const patchAgentWorkhour = (data) => {
  const response = Axios.patch("/agents/workhour", data);
  return response;
};
