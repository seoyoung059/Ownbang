// RealEstateSearchBar.jsx
import React, { useState } from "react";
import { Input, Button, Box } from "@mui/material";
import { useTheme } from "@mui/material";
import { useBoundStore } from "../../store/store";

const RealEstateSearchBar = () => {
  const theme = useTheme();
  const [inputValue, setInputValue] = useState("");
  const setSearchTerm = useBoundStore((state) => state.setSearchTerm);

  const onInputChange = (event) => {
    setInputValue(event.target.value);
  };

  const onSearchInputValue = () => {
    setSearchTerm(inputValue); // Update searchTerm in Zustand
    setInputValue("");
  };

  const onKeyDown = (event) => {
    if (event.key === "Enter") {
      onSearchInputValue();
    }
  };

  return (
    <Box sx={{ display: "flex" }}>
      <Input
        type="text"
        placeholder="지역명 + 역명으로 검색하세요"
        value={inputValue}
        onChange={onInputChange}
        onKeyDown={onKeyDown}
        disableUnderline
        sx={{
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
