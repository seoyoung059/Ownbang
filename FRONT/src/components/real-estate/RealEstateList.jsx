import React, { useState, useEffect, useRef } from "react";
import { List, Box, Typography } from "@mui/material";
import { useTheme } from "@mui/material";
import { SearchOff } from "@mui/icons-material";
import RealEstateItem from "./RealEstateItem";

const RealEstateList = ({
  markers,
  onSelectItem,
  selectedItem, // 추가: 선택된 아이템을 표시하기 위한 prop
  bookmarkList,
  getBookmarks,
  toggleBookmarks,
}) => {
  const theme = useTheme();
  const [displayedMarkers, setDisplayedMarkers] = useState([]);

  // 각 아이템의 참조를 저장하기 위한 객체
  const itemRefs = useRef({});

  useEffect(() => {
    setDisplayedMarkers(markers);
  }, [markers]);

  // 선택된 아이템이 리스트에 있을 경우 스크롤 이동
  useEffect(() => {
    if (selectedItem && itemRefs.current[selectedItem.id]) {
      const element = itemRefs.current[selectedItem.id];

      element.scrollIntoView({
        behavior: "smooth",
        block: "center",
        inline: "nearest",
      });

      // 추가적인 오프셋을 적용하여 헤더에 가려지지 않도록 조정
      const offset = 10; // 원하는 오프셋 값, 헤더 높이만큼 조정
      const scrolledY = window.scrollY;

      if (scrolledY) {
        window.scroll({
          top: scrolledY - offset,
          behavior: "smooth",
        });
      }
    }
  }, [selectedItem]);

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
          gap: 1,
        }}
      >
        {displayedMarkers &&
          displayedMarkers.map((marker) => (
            <div
              key={marker.id}
              ref={(el) => (itemRefs.current[marker.id] = el)} // 각 아이템의 ref 저장
            >
              <RealEstateItem
                marker={marker}
                toggleFavorite={() => toggleFavorite(marker.id)}
                onClick={() => onSelectItem(marker)}
                selected={selectedItem && selectedItem.id === marker.id} // 선택된 항목 강조
              />
            </div>
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
