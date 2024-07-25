import React, { useState } from "react";
import RealEstateMap from "../components/real-estate/RealEstateMap";
import RealEstateSearchBar from "../components/real-estate/RealEstateSearchBar";
import { Box } from "@mui/material";
import { useTheme } from "@mui/material";

const RealEstatePage = () => {
  const theme = useTheme();
  const [searchTerm, setSearchTerm] = useState("");
  console.log(searchTerm);
  // 검색어를 업데이트하는 함수
  const handleSearch = (term) => {
    setSearchTerm(term);
  };

  return (
    <div style={{ paddingTop: "80px", position: "relative" }}>
      <Box
        sx={{
          position: "absolute",
          zIndex: "999",
          padding: "20px",
        }}
      >
        <RealEstateSearchBar onSearch={handleSearch} />
      </Box>
      <RealEstateMap
        searchTerm={searchTerm}
        sx={{ position: "absolute" }}
        size="900px"
      />
    </div>
  );
};
export default RealEstatePage;
