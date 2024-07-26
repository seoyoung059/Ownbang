import React from "react";
import List from "@mui/material/List";
import RealEstateItem from "./RealEstateItem";
import clusterPositionsData from "./clusterPositionsData.json";

const RealEstateList = () => {
  return (
    <List sx={{ width: "100%", maxWidth: 360 }}>
      {clusterPositionsData.tmpMarkers.map((marker, index) => (
        <RealEstateItem key={index} marker={marker} />
      ))}
    </List>
  );
};

export default RealEstateList;
