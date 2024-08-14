import React, { useState } from "react";
import { Box, IconButton, Card, CardMedia, CardContent } from "@mui/material";
import { ChevronLeft, ChevronRight } from "@mui/icons-material";

const RealEstateImages = ({ images, user }) => {
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

  // 길이가 2 이상일 때만 화살표를 표시
  const showArrows = images.length > 1;

  return (
    <Box
      sx={{
        width: "100%",
        margin: "auto",
        position: "relative",
      }}
    >
      <Card sx={{ position: "relative", overflow: "hidden" }}>
        {!user.isAgent && showArrows && (
          <>
            {/* 이전 이미지 버튼 */}
            <IconButton
              onClick={onPrevImg}
              sx={{
                position: "absolute",
                left: "0%", // 사진의 왼쪽 끝에 배치
                top: "50%",
                transform: "translateY(-50%)",
                zIndex: 1,
                backgroundColor: "rgba(0, 0, 0, 0.5)",
                color: "white",
                height: "50px",
                width: "50px",
                "&:hover": {
                  backgroundColor: "rgba(0, 0, 0, 0.7)",
                },
              }}
            >
              <ChevronLeft />
            </IconButton>

            {/* 다음 이미지 버튼 */}
            <IconButton
              onClick={onNextImg}
              sx={{
                position: "absolute",
                right: "0%", // 사진의 오른쪽 끝에 배치
                top: "50%",
                transform: "translateY(-50%)",
                zIndex: 1,
                backgroundColor: "rgba(0, 0, 0, 0.5)",
                color: "white",
                height: "50px",
                width: "50px",
                "&:hover": {
                  backgroundColor: "rgba(0, 0, 0, 0.7)",
                },
              }}
            >
              <ChevronRight />
            </IconButton>
          </>
        )}

        {/* 현재 인덱스의 이미지를 렌더링 */}
        <CardMedia
          component="img"
          image={images[currentIndex]}
          alt={`Image ${currentIndex + 1}`}
          sx={{ height: "300px", width: "100%", objectFit: "cover" }}
        />

        <CardContent
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
          }}
        />
      </Card>
    </Box>
  );
};

export default RealEstateImages;
