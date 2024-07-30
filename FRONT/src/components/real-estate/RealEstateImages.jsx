import React, { useState } from "react";
import { Box, IconButton, Card, CardMedia, CardContent } from "@mui/material";
import { ChevronLeft, ChevronRight } from "@mui/icons-material";

// 이미지 배열
const images = [
  "https://via.placeholder.com/800x400?text=Image+1",
  "https://via.placeholder.com/800x400?text=Image+2",
  "https://via.placeholder.com/800x400?text=Image+3",
  // 추가 이미지 URL
];

const RealEstateImages = () => {
  const [currentIndex, setCurrentIndex] = useState(0);

  const onPrevImg = () => {
    setCurrentIndex((prevIndex) =>
      prevIndex === 0 ? images.length - 1 : prevIndex - 1
    );
  };

  const onNextImg = () => {
    setCurrentIndex((prevIndex) =>
      prevIndex === images.length - 1 ? 0 : prevIndex + 1
    );
  };

  return (
    <Box
      sx={{
        width: "100%",
        margin: "auto",
        position: "relative",
      }}
    >
      <Card sx={{ position: "relative", overflow: "hidden" }}>
        {/* 앞으로 이동하는 버튼 */}
        <IconButton
          onClick={onPrevImg}
          sx={{
            position: "absolute",
            left: 0,
            top: "50%",
            transform: "translateY(-50%)",
          }}
        >
          <ChevronLeft />
        </IconButton>
        <CardMedia
          component="img"
          image={images[currentIndex]}
          alt={`Image ${currentIndex + 1}`}
        />
        <CardContent
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
          }}
        />
        {/* 다음 사진으로 넘기는 버튼 */}
        <IconButton
          onClick={onNextImg}
          sx={{
            position: "absolute",
            right: 0,
            top: "50%",
            transform: "translateY(-50%)",
          }}
        >
          <ChevronRight />
        </IconButton>
      </Card>
    </Box>
  );
};

export default RealEstateImages;
