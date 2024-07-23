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

export default function MyBookmarkItem({ bookmark }) {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));
  return (
    <Card
      sx={{
        display: "flex",
        mb: 2,
        width: isMobile ? "100%" : "600px",
        bgcolor: theme.palette.common.white,
      }}
    >
      <CardMedia
        component="img"
        sx={{ width: isMobile ? 140 : 160 }}
        image={bookmark.image}
        alt={bookmark.title}
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
              {bookmark.title}
            </Typography>
            <IconButton aria-label="bookmark">
              <Bookmark sx={{ color: theme.palette.bookmark }} />
            </IconButton>
          </Box>
          <Typography
            variant="subtitle1"
            color="text.secondary"
            component="div"
            sx={{ fontSize: theme.typography.fontSize }}
          >
            {bookmark.size} · {bookmark.floor}
          </Typography>
          <Typography
            variant="body2"
            color="text.secondary"
            component="div"
            sx={{ fontSize: theme.typography.fontSize }}
          >
            {bookmark.location}
          </Typography>
          <Typography
            variant="body2"
            color="text.secondary"
            component="div"
            sx={{ fontSize: theme.typography.fontSize }}
          >
            {bookmark.description}
          </Typography>
        </CardContent>
      </Box>
    </Card>
  );
}
