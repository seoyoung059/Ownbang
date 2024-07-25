import { Box, Input } from "@mui/material";
import CheckListItem from "./CheckListItem";
import { useState } from "react";
import { useTheme } from "@mui/material";

const List = ({ checkitems, onUpdate, onDelete }) => {
  const theme = useTheme();
  const [search, setSearch] = useState("");

  const onChangeSearch = (e) => {
    setSearch(e.target.value);
  };

  const getFilteredData = () => {
    if (search === "") {
      return checkitems;
    }
    return checkitems.filter((checkitem) =>
      checkitem.content.toLowerCase().includes(search.toLowerCase())
    );
  };

  const filteredCheckItems = getFilteredData();

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        gap: "20px",
        justifyContent: "center",
      }}
    >
      {/* checkList 항목 검색란 */}
      {/* <Box
        sx={{
          display: "flex",
          justifyContent: "flex-end", // Align input to the right
          marginBottom: "20px", // Space below the search input
        }}
      >
        <Input
          placeholder="검색어를 입력하세요"
          value={search}
          onChange={onChangeSearch}
          sx={{
            width: {
              xs: "150px", // Adjust for small screens
              sm: "40%", // Adjust for larger screens
            },
            paddingTop: "15px",
            paddingLeft: "10px",
            paddingBottom: "15px",
            fontSize: theme.fontSize.small,
          }}
        />
      </Box> */}

      {/* 아이템 각각 */}
      <Box
        className="listItem-wrapper"
        sx={{
          display: "flex",
          flexDirection: "column",
          gap: "20px",
        }}
      >
        {filteredCheckItems.map((checkitem) => (
          <CheckListItem
            onUpdate={onUpdate}
            onDelete={onDelete}
            key={checkitem.id}
            {...checkitem}
          />
        ))}
      </Box>
    </Box>
  );
};

export default List;
