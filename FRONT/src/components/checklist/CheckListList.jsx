import React from "react";
import CheckListItem from "./CheckListItem";

const CheckListList = ({ contents, onUpdate, onDelete, canEdit }) => {
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
          onUpdate={onUpdate}
          onDelete={onDelete}
          canEdit={canEdit}
        />
      ))}
    </>
  );
};

export default CheckListList;
