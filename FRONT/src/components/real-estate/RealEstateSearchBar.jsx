// RealEstateSearchBar.jsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Input, Button, Box } from "@mui/material";
import { useTheme } from "@mui/material";
import { useBoundStore } from "../../store/store";

const RealEstateSearchBar = ({ onSearch, isMain }) => {
  const theme = useTheme();
  const navigate = useNavigate();
  const [inputValue, setInputValue] = useState("");
  const setSearchTerm = useBoundStore((state) => state.setSearchTerm);

  const onInputChange = (event) => {
    setInputValue(event.target.value);
    onSearch(event.target.value);
  };

  const onSearchInputValue = () => {
    setSearchTerm(inputValue); // Update searchTerm in Zustand
    navigate(`/estate?search=${inputValue}`, { replace: true });
    setInputValue("");
  };

  const onKeyDown = (event) => {
    if (event.key === "Enter") {
      onSearchInputValue();
    }
  };

  return (
    <Box sx={{ display: "flex", width: isMain ? "80%" : "auto" }}>
      <Input
        type="text"
        placeholder={
          isMain
            ? "원하시는 원룸의 지역명, 지하철역, 단지명(아파트명)을 입력해주세요"
            : "지역명 + 역명으로 검색하세요"
        }
        value={inputValue}
        onChange={onInputChange}
        onKeyDown={onKeyDown}
        disableUnderline
        sx={{
          flex: 10,
          marginRight: "8px",
          borderRadius: "7px",
          border: "1px solid lightgray",
          padding: "15px 25px",
          backgroundColor: theme.palette.common.white,
          fontSize: theme.fontSize.medium,
          boxShadow: 3,
        }}
      />
      <Button
        variant="contained"
        onClick={onSearchInputValue}
        sx={{
          flex: 1,
          marginRight: "8px",
          borderRadius: "7px",
          padding: "13px 15px",
          backgroundColor: theme.palette.primary.dark,
          fontSize: theme.fontSize.medium,
        }}
      >
        검색
      </Button>
    </Box>
  );
};

export default RealEstateSearchBar;
