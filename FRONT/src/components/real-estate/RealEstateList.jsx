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
  const [displayedMarkers, setDisplayedMarkers] = useState([]);

  useEffect(() => {
    setDisplayedMarkers(markers);
  }, [markers]);

  const toggleFavorite = async (id) => {
    try {
      await toggleBookmarks(id); // 북마크 API 호출
      setDisplayedMarkers((prevMarkers) =>
        prevMarkers.map((marker) =>
          marker.id === id ? { ...marker, favorite: !marker.favorite } : marker
        )
      );
      await getBookmarks(); // 업데이트된 북마크 리스트 가져오기
    } catch (error) {
      console.error("Error toggling favorite:", error);
    }
  };

  return (
    <Box
      sx={{
        height: "100%",
        overflowY: "auto", // 스크롤을 이 곳에서만 발생
        bgcolor: theme.palette.background.default,
        paddingTop: 10,
      }}
    >
      <List
        sx={{
          width: "100%",
          display: "flex",
          flexDirection: "column",
          gap: 2,
        }}
      >
        {displayedMarkers &&
          displayedMarkers.map((marker) => (
            <RealEstateItem
              key={marker.id}
              marker={marker}
              toggleFavorite={() => toggleFavorite(marker.id)}
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
              height: "100%",
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
    </Box>
  );
};

export default RealEstateList;
