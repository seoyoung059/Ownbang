import React, { useState } from "react";
import RealEstateMap from "../components/real-estate/RealEstateMap";
import RealEstateSearchBar from "../components/real-estate/RealEstateSearchBar";
import RealEstateList from "../components/real-estate/RealEstateList";
import RealEstateDetail from "../components/real-estate/RealEstateDetail";
import Reservation from "../components/real-estate/Reservation";
import { Box, IconButton } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { useTheme } from "@mui/material";

const RealEstatePage = () => {
  const theme = useTheme();
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedItem, setSelectedItem] = useState(null);
  const [showReservation, setShowReservation] = useState(false);
  const [visibleMarkers, setVisibleMarkers] = useState([]);

  const onSearch = (term) => {
    setSearchTerm(term);
  };

  const onSelectItem = (item) => {
    setSelectedItem(item);
    setShowReservation(false); // 새로운 아이템이 선택되었을 때 예약 페이지 닫기
  };

  const onCloseDetailCard = () => {
    setSelectedItem(null);
    setShowReservation(false); // 새로운 디테일 카드가 선태되었을 때 예약 페이지 닫기
  };

  const onOpenReservationCard = () => {
    setShowReservation(true);
  };

  const onCloseReservationCard = () => {
    setShowReservation(false);
  };

  const onBoundsChange = (markers) => {
    setVisibleMarkers(markers);
  };

  const onMarkerClick = (marker) => {
    onSelectItem(marker); // 마커를 눌러서 Detail 열기 가능
  };

  return (
    <Box sx={{ display: "flex", height: "100vh", position: "relative" }}>
      <Box
        sx={{
          marginRight: "5px",
          paddingTop: "80px",
          width: "20%",
          height: "100vh",
          overflow: "auto", // 목록만 스크롤 가능하게
          position: "relative",
        }}
      >
        <RealEstateList markers={visibleMarkers} onSelectItem={onSelectItem} />
      </Box>

      <Box sx={{ paddingTop: "80px", width: "80%" }}>
        <Box
          sx={{
            position: "fixed",
            zIndex: "999",
            padding: "20px",
            right: 0,
            top: 100,
          }}
        >
          <RealEstateSearchBar onSearch={onSearch} />
        </Box>
        <RealEstateMap
          searchTerm={searchTerm}
          onBoundsChange={onBoundsChange}
          onSelectMarker={onMarkerClick}
        />
      </Box>

      {selectedItem && (
        <Box
          sx={{
            position: "fixed",
            top: 0,
            right: 0,
            width: "100%",
            height: "100%",
            backgroundColor: "rgba(0, 0, 0, 0)", // 배경 투명
            zIndex: 1000,
          }}
          onClick={onCloseDetailCard}
        >
          <Box
            sx={{
              position: "absolute",
              top: "10%",
              right: "79.7%",
              transform: "translateX(101%)",
              backgroundColor: theme.palette.background.default,
              padding: 3,
              borderRadius: 1,
              boxShadow: 3,
              width: "400px",
              height: "80%",
              overflow: "auto",
              zIndex: 50,
            }}
            onClick={(e) => e.stopPropagation()} // 디테일 카드 닫기에서 제외
          >
            <IconButton
              onClick={onCloseDetailCard}
              sx={{
                position: "absolute",
                top: 8,
                right: 8, // X 이동 시키기
              }}
            >
              <CloseIcon />
            </IconButton>
            <RealEstateDetail
              item={selectedItem}
              onOpenReservationCard={onOpenReservationCard}
            />
          </Box>

          {showReservation && (
            <Box
              sx={{
                position: "absolute",
                top: "10%",
                right: "56%",
                transform: "translateX(101%)",
                backgroundColor: theme.palette.background.default,
                padding: 3,
                borderRadius: 1,
                boxShadow: 3,
                width: "400px",
                height: "80%",
                overflow: "auto",
                zIndex: 50,
              }}
              onClick={(e) => e.stopPropagation()} // 카드 닫히기에서 제외
            >
              <IconButton
                onClick={onCloseReservationCard} // 카드 닫기
                sx={{
                  position: "absolute",
                  top: 8,
                  right: 8, // X 이동시키기
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
