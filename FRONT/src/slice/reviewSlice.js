import { postReview, deleteReview } from "../api/review";

export const createReviewSlice = (set) => ({
  writeReview: async (data) => {
    const result = await postReview(data);
    console.log(result);
  },
  killReview: async (data) => {
    const result = await deleteReview(data);
  },
});
