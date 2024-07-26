import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import List from "@mui/material/List";
import RealEstateItem from "./RealEstateItem";
import clusterPositionsData from "./clusterPositionsData.json";

const RealEstateList = ({ onSelectItem }) => {
  const [markers, setMarkers] = useState(clusterPositionsData.tmpMarkers);

  const toggleFavorite = (index) => {
    const newMarkers = markers.map((marker, i) => {
      if (i === index) {
        const updatedMarker = { ...marker, favorite: !marker.favorite };
        console.log(`${marker.title} - favorite: ${updatedMarker.favorite}`);
        return updatedMarker;
      }
      return marker;
    });
    setMarkers(newMarkers);
  };

  return (
    <List sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}>
      {markers.map((marker, index) => (
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
