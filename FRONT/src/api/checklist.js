import Axios from "./index";

export const getChecklists = async () => {
  const response = await Axios.get("/checklists");
  return response.data;
};

export const pathChecklists = async (id, data) => {
  const response = await Axios.patch(`/checklists/${id}`, data);
  return response.data;
};

export const addChecklist = async (data) => {
  const response = await Axios.post("/checklists/template", data);
  return response.data;
};

export const addChecklist_video = async (id, data) => {
  const response = await Axios.post("/checklists/non-template", {
    reservationId: id,
    ...data,
  });
};

export const deleteChecklist = async (id) => {
  const response = await Axios.delete(`/checklists/${id}`);
  console.log(response);
  return response.data;
};
