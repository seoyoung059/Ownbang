import { postReview } from "../api/review";

export const createReviewSlice = (set) => ({
  writeReview: async (data) => {
    const result = await postReview(data);
    console.log(result.data);
  },
});
