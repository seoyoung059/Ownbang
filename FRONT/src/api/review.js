import Axios from "./index";

export const postReview = (data) => {
  const response = Axios.post("/reviews", data);
  return response.data;
};

export const deleteReview = (id) => {
  const response = Axios.delete(`/reviews/${id}`);
  return response.data;
};
