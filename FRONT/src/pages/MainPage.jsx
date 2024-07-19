import React from "react";

import { useBoundStore } from "../store/store";

// zustand 사용 예시
export default function MainPage() {
  const checkList = useBoundStore((state) => state.checklist);
  const realEstate = useBoundStore((state) => state.value);
  return (
    <>
      <div>MainPage</div>
      <div>{checkList}</div>
      <div>{realEstate}</div>
    </>
  );
}
