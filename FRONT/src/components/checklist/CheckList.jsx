import { useState, useRef } from "react";
import { Box } from "@mui/material";
import CheckListTitle from "./CheckListTitle";
import CheckListList from "./CheckListList";
import CheckListAddInput from "./CheckListAddInput";
import tmpData from "./CheckListData.json";

const CheckList = () => {
  const [checkitems, setCheckItems] = useState(tmpData);
  const [selectedTitle, setSelectedTitle] = useState(null);
  const idRef = useRef(3);

  const onCreate = (content) => {
    const newCheckItem = {
      id: idRef.current++,
      isDone: false,
      content: content,
    };
    setCheckItems([newCheckItem, ...checkitems]);
  };

  const onUpdate = (targetId) => {
    setCheckItems(
      checkitems.map((checkitem) =>
        checkitem.id === targetId
          ? { ...checkitem, isDone: !checkitem.isDone }
          : checkitem
      )
    );
  };

  const onDelete = (targetId) => {
    setCheckItems(checkitems.filter((checkitem) => checkitem.id !== targetId));
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        gap: "20px",
        maxWidth: "600px",
        border: "1px solid lightGray",
        borderRadius: "10px",
        padding: "30px",
      }}
    >
      <Box sx={{ display: "flex", justifyContent: "center" }}>
        <CheckListTitle setSelectedTitle={setSelectedTitle} />
      </Box>

      {/* CheckList 항목 체크, 내용 확인, 삭제 */}
      {selectedTitle && (
        <Box sx={{ marginTop: "30px" }}>
          <CheckListList
            checkitems={checkitems}
            onUpdate={onUpdate}
            onDelete={onDelete}
          />
        </Box>
      )}

      {/* 새로운 항목 추가 */}
      {selectedTitle && (
        <Box sx={{ marginTop: "40px" }}>
          <CheckListAddInput onCreate={onCreate} />
        </Box>
      )}
    </Box>
  );
};

export default CheckList;
