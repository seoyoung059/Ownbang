import Axios from "./index";

export const getSearchList = async (word) => {
  const response = await Axios.get(`/search?searchName=${word}`);
  return response.data;
};
