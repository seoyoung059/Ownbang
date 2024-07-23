import React, { useState } from "react";
import RealEstateMap from "../components/real-estate/RealEstateMap";
import RealEstateSearchBar from "../components/real-estate/RealEstateSearchBar";
import { Box } from "@mui/material";

const RealEstatePage = () => {
  const [searchTerm, setSearchTerm] = useState("");
  console.log(searchTerm);
  // 검색어를 업데이트하는 함수
  const handleSearch = (term) => {
    setSearchTerm(term);
  };

  return (
    <div style={{ paddingTop: "80px", position: "relative" }}>
      <Box
        style={{
          position: "absolute",
          top: "20px",
          left: "50%",
          transform: "translateX(-50%)",
          zIndex: 10,
        }}
      >
        <RealEstateSearchBar onSearch={handleSearch} />
      </Box>
      <RealEstateMap searchTerm={searchTerm} />
    </div>
  );
};
export default RealEstatePage;
