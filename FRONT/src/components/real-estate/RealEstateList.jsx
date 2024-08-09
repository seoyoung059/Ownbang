import React, { useState, useEffect } from "react";
import { List, Box, Typography } from "@mui/material";
import { useTheme } from "@mui/material";
import { SearchOff } from "@mui/icons-material";
import RealEstateItem from "./RealEstateItem";

const RealEstateList = ({
  markers,
  onSelectItem,
  bookmarkList,
  getBookmarks,
  toggleBookmarks,
}) => {
  const theme = useTheme();
  const [displayedMarkers, setDisplayedMarkers] = useState();

  useEffect(() => {
    setDisplayedMarkers(markers);
  }, [markers]);

  const toggleFavorite = (id) => {
    setDisplayedMarkers((prevMarkers) =>
      prevMarkers.map((marker) => {
        if (marker.id === id) {
          const updatedMarker = { ...marker, favorite: !marker.favorite };
          toggleBookmarks(updatedMarker.id);
          return updatedMarker;
        }
        return marker;
      })
    );
    getBookmarks();
  };

  return (
    <List
      sx={{
        width: "100%",
        maxWidth: 360,
        bgcolor: theme.palette.background.default,
      }}
    >
      {displayedMarkers &&
        displayedMarkers.map((marker, index) => (
          <RealEstateItem
            key={index}
            marker={marker}
            toggleFavorite={() => toggleFavorite(index)}
            onClick={() => onSelectItem(marker)}
          />
        ))}
      {displayedMarkers && !displayedMarkers.length && (
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            mt: 12,
            height: "100vh",
            textAlign: "center",
            p: 3,
          }}
        >
          <SearchOff sx={{ fontSize: 60, color: "gray", mb: 2 }} />
          <Typography variant="h5" component="div" gutterBottom>
            찾는 매물이 없습니다.
          </Typography>
          <Typography variant="body1" component="div" gutterBottom>
            현재 지역에 매물이 없습니다. <br />
            다른 지역을 찾아보세요.
          </Typography>
        </Box>
      )}
    </List>
  );
};

export default RealEstateList;
