import React from "react";
import CheckListItem from "./CheckListItem";

const CheckListList = ({
  contents,
  onUpdate,
  onDelete,
  canEdit,
  onTimestampClick,
  forReplay,
}) => {
  if (!contents) {
    return null;
  }

  return (
    <>
      {Object.keys(contents).map((key) => (
        <CheckListItem
          key={key}
          id={key}
          isDone={contents[key] !== ""}
          content={key}
          timestamp={contents[key]} // 타임스탬프 전달
          onUpdate={onUpdate}
          onDelete={onDelete}
          canEdit={canEdit}
          onTimestampClick={onTimestampClick}
          forReplay={forReplay}
        />
      ))}
    </>
  );
};

export default CheckListList;
