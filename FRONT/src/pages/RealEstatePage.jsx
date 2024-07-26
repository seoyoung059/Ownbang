import React, { useState } from "react";
import RealEstateMap from "../components/real-estate/RealEstateMap";
import RealEstateSearchBar from "../components/real-estate/RealEstateSearchBar";
import RealEstateList from "../components/real-estate/RealEstateList";
import RealEstateDetail from "../components/real-estate/RealEstateDetail";
import Reservation from "../components/real-estate/Reservation";
import {
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  IconButton,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { useTheme } from "@mui/material";

const RealEstatePage = () => {
  const theme = useTheme();
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedItem, setSelectedItem] = useState(null);

  // 검색어를 업데이트하는 함수
  const handleSearch = (term) => {
    setSearchTerm(term);
  };

  // 매물 리스트에서 선택하는 항목
  const handleSelectItem = (item) => {
    setSelectedItem(item);
  };

  // Close the detail card
  const handleCloseDetail = () => {
    setSelectedItem(null);
  };

  return (
    <Box sx={{ display: "flex", height: "100vh" }}>
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
          height: "100vh",
          overflow: "auto", // RealEstateList에만 스크롤 적용
        }}
      >
        <RealEstateList onSelectItem={handleSelectItem} />
      </Box>

      {/* Conditional rendering for detail card */}
      {selectedItem && (
        <Box
          sx={{
            position: "fixed",
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            display: "flex",
            backgroundColor: "rgba(0, 0, 0, 0.5)", // Semi-transparent background
            zIndex: 1000,
          }}
        >
          <Box
            sx={{
              backgroundColor: theme.palette.background.paper,
              padding: 3,
              borderRadius: 1,
              border: "1px solid darkGray",
              width: "500px", // Fixed width
              maxHeight: "80vh",
              overflow: "auto",
              position: "relative",
              top: 80,
              left: 1000,
            }}
          >
            <IconButton
              onClick={handleCloseDetail}
              sx={{
                position: "absolute",
                top: 8,
                right: 8,
              }}
            >
              <CloseIcon />
            </IconButton>
            <RealEstateDetail item={selectedItem} />
          </Box>
        </Box>
      )}
    </Box>
  );
};

export default RealEstatePage;
