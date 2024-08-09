import { create } from "zustand";

import { createCheckListSlice } from "../slice/checkListSlice";
import { createRealEstateSlice } from "../slice/realEstateSlice";
import { createUserSlice } from "../slice/userSlice";
import { createSearchSlice } from "../slice/searchSlice";
import { createAuthSlice } from "../slice/authSlice";
import { createReservation } from "../slice/reservationSlice";
import { createAgentSlice } from "../slice/agentSlice";
import { createVideoSlice } from "../slice/videoSlice";
import { createReviewSlice } from "../slice/reviewSlice";
import { createBookmarks } from "../slice/bookmarksSlice";

export const useBoundStore = create((...a) => ({
  ...createCheckListSlice(...a),
  ...createRealEstateSlice(...a),
  ...createUserSlice(...a),
  ...createSearchSlice(...a),
  ...createAuthSlice(...a),
  ...createReservation(...a),
  ...createAgentSlice(...a),
  ...createVideoSlice(...a),
  ...createReviewSlice(...a),
  ...createBookmarks(...a),
}));
