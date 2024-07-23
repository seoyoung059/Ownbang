import React, { useState } from "react";
import { Input, Button } from "@mui/material";

const RealEstateSearchBar = ({ onSearch }) => {
  const [inputValue, setInputValue] = useState("");

  const onInputChange = (event) => {
    setInputValue(event.target.value);
  };

  const onSearchInputValue = () => {
    // 검색어를 다른 함수로 전달
    if (onSearch) {
      onSearch(inputValue);
    }
    // 검색 후 입력 창 비우기
    setInputValue("");
  };

  const onKeyDown = (event) => {
    if (event.key === "Enter") {
      onSearchInputValue();
    }
  };

  return (
    <div style={{ display: "flex", alignItems: "center" }}>
      <Input
        className="searchInputValue"
        type="text"
        placeholder="검색어를 입력하세요."
        value={inputValue}
        onChange={onInputChange}
        onKeyDown={onKeyDown}
        disableUnderline // 기본 CSS에 있는 밑줄 제거
        style={{
          marginRight: "8px",
          borderRadius: "7px", // 둥근 사각형 모양
          border: "1px solid lightgray", // 연한 회색 테두리
          padding: "4px 8px",
        }}
      />
      <Button variant="contained" color="primary" onClick={onSearchInputValue}>
        검색
      </Button>
    </div>
  );
};

export default RealEstateSearchBar;
