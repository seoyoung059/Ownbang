import React, { useState } from "react";
import RealEstateMap from "../components/real-estate/RealEstateMap";
import RealEstateSearchBar from "../components/real-estate/RealEstateSearchBar";
import RealEstateList from "../components/real-estate/RealEstateList";
import RealEstateDetail from "../components/real-estate/RealEstateDetail";
import Reservation from "../components/real-estate/Reservation";
import { Box, IconButton, Button } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { useTheme } from "@mui/material";

const RealEstatePage = () => {
  const theme = useTheme();
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedItem, setSelectedItem] = useState(null);
  const [showReservation, setShowReservation] = useState(false);

  // 검색어를 업데이트하는 함수
  const handleSearch = (term) => {
    setSearchTerm(term);
  };

  // 매물 리스트에서 선택하는 항목
  const handleSelectItem = (item) => {
    setSelectedItem(item);
    setShowReservation(false); // 새로운 아이템 선택 시 예약 창 닫기
  };

  // 디테일 카드 닫기
  const handleCloseDetail = () => {
    setSelectedItem(null);
    setShowReservation(false); // 디테일 카드 닫을 때 예약 창도 닫기
  };

  // 예약하기 버튼 클릭 핸들러
  const handleReservationClick = () => {
    setShowReservation(true);
  };

  // 예약 카드 닫기
  const handleCloseReservation = () => {
    setShowReservation(false);
  };

  return (
    <Box sx={{ display: "flex", height: "100vh", position: "relative" }}>
      {/* 리스트 */}
      <Box
        sx={{
          marginRight: "5px",
          paddingTop: "80px",
          width: "20%",
          height: "100vh",
          overflow: "auto", // RealEstateList에만 스크롤 적용
          position: "relative",
        }}
      >
        <RealEstateList onSelectItem={handleSelectItem} />
      </Box>

      {/* 지도와 검색 바 */}
      <Box sx={{ paddingTop: "80px", width: "80%" }}>
        {/* 검색 바를 오른쪽으로 이동 */}
        <Box
          sx={{
            position: "fixed",
            zIndex: "999",
            padding: "20px",
            right: 0,
            top: 100,
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

      {/* List에서 Item을 누르면 Detail 카드가 뜹니다 */}
      {selectedItem && (
        <Box
          sx={{
            position: "fixed",
            top: 0,
            right: 0,
            width: "100%",
            height: "100%",
            backgroundColor: "rgba(0, 0, 0, 0)", // 배경 색 투명
            zIndex: 1000,
          }}
          onClick={handleCloseDetail}
        >
          <Box
            sx={{
              position: "absolute",
              top: "10%",
              right: "79.7%",
              transform: "translateX(101%)",
              backgroundColor: theme.palette.background.paper,
              padding: 3,
              borderRadius: 1,
              boxShadow: 3,
              width: "400px",
              height: "80%",
              overflow: "auto",
              zIndex: 50,
            }}
            onClick={(e) => e.stopPropagation()} // 디테일 창 닫기에서 제외
          >
            <IconButton
              onClick={handleCloseDetail}
              sx={{
                position: "absolute",
                top: 8,
                right: 8, // X 아이콘을 카드의 우측 상단으로 이동
              }}
            >
              <CloseIcon />
            </IconButton>
            <RealEstateDetail item={selectedItem} />
            <Button onClick={handleReservationClick}>예약하기</Button>
          </Box>

          {showReservation && (
            <Box
              sx={{
                position: "absolute",
                top: "10%",
                right: "56%",
                transform: "translateX(101%)",
                backgroundColor: theme.palette.background.paper,
                padding: 3,
                borderRadius: 1,
                boxShadow: 3,
                width: "400px",
                height: "80%",
                overflow: "auto",
                zIndex: 50,
              }}
              onClick={(e) => e.stopPropagation()} // 예약 창 닫기에서 제외
            >
              <IconButton
                onClick={handleCloseReservation} // 예약 카드 닫기 핸들러
                sx={{
                  position: "absolute",
                  top: 8,
                  right: 8, // X 아이콘을 카드의 우측 상단으로 이동
                }}
              >
                <CloseIcon />
              </IconButton>
              <Reservation />
            </Box>
          )}
        </Box>
      )}
    </Box>
  );
};

export default RealEstatePage;
