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
    <div style={{ paddingTop: "80px" }}>
      <Box sx={{ marginBottom: "10px" }}>
        <RealEstateSearchBar onSearch={handleSearch} />
      </Box>
      <RealEstateMap searchTerm={searchTerm} />
    </div>
  );
};
export default RealEstatePage;
