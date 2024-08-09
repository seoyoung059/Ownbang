// RealEstateItem.jsx
import React, { useState, useEffect } from "react";
import {
  Card,
  CardContent,
  CardMedia,
  Typography,
  IconButton,
  Box,
} from "@mui/material";
import { Bookmark, BookmarkBorder } from "@mui/icons-material";
import { useTheme } from "@mui/material/styles";

const RealEstateItem = ({ marker, toggleFavorite, onClick }) => {
  const theme = useTheme();

  // 서버에서 받은 isBookmarked 값을 클라이언트 favorite 상태로 설정
  const [isFavorite, setIsFavorite] = useState(marker.isBookmarked);

  useEffect(() => {
    setIsFavorite(marker.isBookmarked);
  }, [marker.isBookmarked]);

  const handleToggleFavorite = (e) => {
    e.stopPropagation(); // Prevent click event from bubbling up to Card
    setIsFavorite(!isFavorite); // 클라이언트 상태 업데이트
    toggleFavorite(); // 서버 상태 업데이트
  };

  return (
    <Card
      sx={{
        display: "flex",
        marginBottom: "20px",
        position: "relative",
        backgroundColor: theme.palette.background.default,
        borderBottom: "1px solid",
        borderColor: theme.palette.action.disabled,
        borderRadius: 0,
      }}
      elevation={0}
      onClick={onClick}
    >
      <CardMedia
        component="img"
        sx={{ width: 150, height: 150 }}
        image={marker.profileImageUrl} // 실제 이미지 URL로 변경
        alt={marker.profileImageUrl}
      />
      <CardContent
        sx={{
          display: "flex",
          flexDirection: "row",
          alignItems: "flex-start",
          paddingRight: "40px",
        }}
      >
        <Box>
          <Typography component="div">
            {marker.dealType} {marker.deposit}/{marker.monthlyRent}
          </Typography>
          <Typography
            variant="subtitle1"
            color="text.secondary"
            component="div"
          >
            {marker.parcel}
          </Typography>
        </Box>
      </CardContent>
      <IconButton
        aria-label="bookmark"
        onClick={handleToggleFavorite}
        sx={{
          position: "absolute",
          top: 8,
          right: 8,
          color: theme.palette.bookmark,
        }}
      >
        {isFavorite ? <Bookmark /> : <BookmarkBorder />}
      </IconButton>
    </Card>
  );
};

export default RealEstateItem;
