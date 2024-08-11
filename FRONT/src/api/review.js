import Axios from "./index";

export const postReview = (data) => {
  const response = Axios.post("/reviews", data);
  return response.data;
};
