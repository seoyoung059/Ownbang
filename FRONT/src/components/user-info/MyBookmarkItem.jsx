import React from "react";
import {
  Box,
  Card,
  CardContent,
  CardMedia,
  Typography,
  IconButton,
} from "@mui/material";
import { useTheme, useMediaQuery } from "@mui/material";
import { Bookmark } from "@mui/icons-material";
import { useBoundStore } from "../../store/store";

export default function MyBookmarkItem({ bookmark }) {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  const { toggleBookmarks, getBookmarks } = useBoundStore((state) => ({
    toggleBookmarks: state.toggleBookmarks,
    getBookmarks: state.getBookmarks,
  }));

  const handleToggleBookmark = async () => {
    try {
      await toggleBookmarks(bookmark.roomInfoSearchResponse.id); // 북마크를 토글
      await getBookmarks();
    } catch (error) {
      console.error("북마크 토글 실패:", error);
    }
  };

  return (
    <Card
      sx={{
        display: "flex",
        mb: 2,
        width: "100%",
        height: 140,
        bgcolor: theme.palette.common.white,
        gap: 2,
      }}
    >
      <CardMedia
        component="img"
        sx={{ width: isMobile ? 140 : 160 }}
        image={bookmark.roomInfoSearchResponse.profileImageUrl}
        alt={
          bookmark.roomInfoSearchResponse.profileImageUrl ||
          "이미지를 불러올 수 없습니다"
        }
      />
      <Box sx={{ display: "flex", flexDirection: "column", flexGrow: 1 }}>
        <CardContent
          sx={{
            flex: "1 0 auto",
          }}
        >
          <Box sx={{ display: "flex", justifyContent: "space-between" }}>
            <Typography
              component="div"
              variant="h6"
              sx={{ fontSize: theme.fontSize.medium }}
            >
              {bookmark.roomInfoSearchResponse.dealType}{" "}
              {bookmark.roomInfoSearchResponse.deposit}/
              {bookmark.roomInfoSearchResponse.monthlyRent}
            </Typography>
            <IconButton aria-label="bookmark" onClick={handleToggleBookmark}>
              <Bookmark sx={{ color: theme.palette.bookmark }} />
            </IconButton>
          </Box>
          <Typography
            variant="subtitle1"
            color="text.secondary"
            component="div"
            sx={{ fontSize: theme.typography.fontSize }}
          >
            위치 : {bookmark.roomInfoSearchResponse.parcel}
          </Typography>
          <Typography
            variant="subtitle1"
            color="text.secondary"
            component="div"
            sx={{ fontSize: theme.typography.fontSize }}
          >
            {bookmark.roomInfoSearchResponse.roomType}·{" "}
            {bookmark.roomInfoSearchResponse.structure}
          </Typography>
          <Typography
            variant="subtitle1"
            color="text.secondary"
            component="div"
            sx={{ fontSize: theme.typography.fontSize }}
          >
            {bookmark.roomInfoSearchResponse.supplyArea} m2 ·{" "}
            {bookmark.roomInfoSearchResponse.roomFloor} 층
          </Typography>
        </CardContent>
      </Box>
    </Card>
  );
}
