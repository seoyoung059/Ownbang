import React, { useState } from "react";
import RealEstateMap from "../components/real-estate/RealEstateMap";
import RealEstateSearchBar from "../components/real-estate/RealEstateSearchBar";
import Reservation from "../components/real-estate/Reservation";
import RealEstateList from "../components/real-estate/RealEstateList";
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
    <Box sx={{ display: "flex" }}>
      <Box sx={{ paddingTop: "80px", position: "relative", width: "80%" }}>
        <Box
          sx={{
            position: "fixed",
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
      </Box>
      <Box
        sx={{
          marginLeft: "10px",
          paddingTop: "80px",
          position: "relative",
          width: "20%",
        }}
      >
        <RealEstateList />
      </Box>
    </Box>
  );
};
export default RealEstatePage;
