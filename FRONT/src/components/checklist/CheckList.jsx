// 체크리스트 구성 각 컴포넌트
// Title, ListList (아래 ListItem), AddInput를
// 합쳐서 렌더링 하는 페이지

import { useState, useRef } from "react";
import { Box } from "@mui/material";
import CheckListTitle from "./CheckListTitle";
import CheckListList from "./CheckListList";
import CheckListAddInput from "./CheckListAddInput";

const tmpData = [
  {
    id: 0,
    isDone: false,
    content: "벽지 곰팡이 확인",
    date: new Date().getTime(),
  },
  {
    id: 1,
    isDone: false,
    content: "수압 확인",
    date: new Date().getTime(),
  },
  {
    id: 2,
    isDone: false,
    content: "풍경 확인",
    date: new Date().getTime(),
  },
];

const CheckList = () => {
  const [checkitems, setCheckItems] = useState(tmpData);
  const idRef = useRef(3);

  const onCreate = (content) => {
    const newCheckItem = {
      id: idRef.current++,
      isDone: false,
      content: content,
      date: new Date().getTime(),
    };
    setCheckItems([newCheckItem, ...checkitems]);
  };

  // 체크박스가 표시됨에 따라 isDone 필드가 토글될 수 있도록
  const onUpdate = (targetId) => {
    setCheckItems(
      checkitems.map((checkitem) =>
        checkitem.id === targetId
          ? { ...checkitem, isDone: !checkitem.isDone }
          : checkitem
      )
    );
  };

  // 항목 삭제
  const onDelete = (targetId) => {
    setCheckItems(checkitems.filter((checkitem) => checkitem.id !== targetId));
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        gap: "20px",
        width: "90%",
      }}
    >
      <Box sx={{ display: "flex", justifyContent: "center" }}>
        <CheckListTitle />
      </Box>

      {/* CheckList 검색/내용/항목 추가 */}
      <CheckListList
        checkitems={checkitems}
        onUpdate={onUpdate}
        onDelete={onDelete}
      />
      <CheckListAddInput onCreate={onCreate} />
    </Box>
  );
};

export default CheckList;
