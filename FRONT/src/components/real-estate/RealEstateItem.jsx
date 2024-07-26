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
import { Bookmark } from "@mui/icons-material";
import { useTheme } from "@mui/material";

const RealEstateItem = ({ marker }) => {
  const theme = useTheme();
  return (
    <Card sx={{ display: "flex", marginBottom: "20px", position: "relative" }}>
      <CardMedia
        component="img"
        sx={{ width: 151 }}
        image="https://via.placeholder.com/150" // 실제 이미지 URL로 변경
        alt={marker.title}
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
          <Typography component="div">{marker.title}</Typography>
          <Typography
            variant="subtitle1"
            color="text.secondary"
            component="div"
          >
            {marker.location}
          </Typography>
        </Box>
      </CardContent>
      <IconButton
        aria-label="bookmark"
        sx={{
          position: "absolute",
          top: 8,
          right: 8,
          color: theme.palette.bookmark,
        }}
      >
        <Bookmark />
      </IconButton>
    </Card>
  );
};
export default RealEstateItem;
