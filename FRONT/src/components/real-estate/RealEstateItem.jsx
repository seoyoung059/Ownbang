// 리스트에 뿌려지는 매물 아이템 카드
import React from "react";
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
            {/* 상가 원룸 투룸 등 */}
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
        onClick={(e) => {
          e.stopPropagation(); // Prevent click event from bubbling up to Card
          toggleFavorite();
        }}
        sx={{
          position: "absolute",
          top: 8,
          right: 8,
          color: theme.palette.bookmark,
        }}
      >
        {marker.favorite ? <Bookmark /> : <BookmarkBorder />}
      </IconButton>
    </Card>
  );
};

export default RealEstateItem;
