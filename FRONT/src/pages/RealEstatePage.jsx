// RealEstatePage.jsx
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
  const [selectedItem, setSelectedItem] = useState(null);
  const [showReservation, setShowReservation] = useState(false);
  const [visibleMarkers, setVisibleMarkers] = useState([]);

  const onSelectItem = (item) => {
    setSelectedItem(item);
    setShowReservation(false);
  };

  const onCloseDetailCard = () => {
    setSelectedItem(null);
    setShowReservation(false);
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
    setSelectedItem(marker);
    setShowReservation(false);
  };

  return (
    <Box sx={{ display: "flex", height: "100vh", position: "relative" }}>
      <Box
        sx={{
          marginRight: "5px",
          paddingTop: "80px",
          width: "20%",
          height: "100vh",
          overflow: "auto",
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
          <RealEstateSearchBar />
        </Box>
        <RealEstateMap
          sx={{ position: "absolute" }}
          size="900px"
          onBoundsChange={onBoundsChange}
          onMarkerClick={onMarkerClick}
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
            backgroundColor: "rgba(0, 0, 0, 0)",
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
            onClick={(e) => e.stopPropagation()}
          >
            <IconButton
              onClick={onCloseDetailCard}
              sx={{
                position: "absolute",
                top: 8,
                right: 8,
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
              onClick={(e) => e.stopPropagation()}
            >
              <IconButton
                onClick={onCloseReservationCard}
                sx={{
                  position: "absolute",
                  top: 8,
                  right: 8,
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
