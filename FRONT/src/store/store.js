import { create } from "zustand";

import { createCheckListSlice } from "../slice/CheckListSlice";
import { createRealEstateSlice } from "../slice/RealEstateSlice";

// zustand 전역 상태 관리 저장소 - 변수와 메서드 모두 객체의 속성으로 정의하듯이 만들면 됨
export const useBoundStore = create((...a) => ({
  ...createCheckListSlice(...a),
  ...createRealEstateSlice(...a),
}));
