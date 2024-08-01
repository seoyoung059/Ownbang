import React, { useState, useEffect } from "react";
import List from "@mui/material/List";
import RealEstateItem from "./RealEstateItem";
import { useTheme } from "@mui/material";

const RealEstateList = ({ markers, onSelectItem }) => {
  const theme = useTheme();
  const [displayedMarkers, setDisplayedMarkers] = useState([]);

  useEffect(() => {
    setDisplayedMarkers(markers);
  }, [markers]);

  const toggleFavorite = (index) => {
    const newMarkers = displayedMarkers.map((marker, i) => {
      if (i === index) {
        const updatedMarker = { ...marker, favorite: !marker.favorite };
        return updatedMarker;
      }
      return marker;
    });
    setDisplayedMarkers(newMarkers);
  };

  return (
    <List
      sx={{
        width: "100%",
        maxWidth: 360,
        bgcolor: theme.palette.background.default,
      }}
    >
      {displayedMarkers.map((marker, index) => (
        <RealEstateItem
          key={index}
          marker={marker}
          toggleFavorite={() => toggleFavorite(index)}
          onClick={() => onSelectItem(marker)}
        />
      ))}
    </List>
  );
};

export default RealEstateList;
