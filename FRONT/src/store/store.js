import { create } from "zustand";

import { createCheckListSlice } from "../slice/checkListSlice";
import { createRealEstateSlice } from "../slice/realEstateSlice";
import { createUserSlice } from "../slice/userSlice";
import { createSearchSlice } from "../slice/searchSlice";
import { createAuthSlice } from "../slice/authSlice";
import { createReservation } from "../slice/reservationSlice";
import { createAgentSlice } from "../slice/agentSlice";
import { createVideoSlice } from "../slice/videoSlice";

// zustand 전역 상태 관리 저장소 - 변수와 메서드 모두 객체의 속성으로 정의하듯이 만들면 됨
export const useBoundStore = create((...a) => ({
  ...createCheckListSlice(...a),
  ...createRealEstateSlice(...a),
  ...createUserSlice(...a),
  ...createSearchSlice(...a),
  ...createAuthSlice(...a),
  ...createReservation(...a),
  ...createAgentSlice(...a),
  ...createVideoSlice(...a),
}));
