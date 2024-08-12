import React, { useEffect, useState } from "react";
import {
  Container,
  Typography,
  Grid,
  CircularProgress,
  Box,
} from "@mui/material";
import MyBookmarkItem from "./MyBookmarkItem";
import { useBoundStore } from "../../store/store";

function MyBookmarkList() {
  const { bookmarkList, getBookmarks } = useBoundStore((state) => ({
    bookmarkList: state.bookmarkList,
    getBookmarks: state.getBookmarks,
  }));

  const [bookmarks, setBookmarks] = useState(bookmarkList);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchBookmarks = async () => {
      setLoading(true);
      try {
        await getBookmarks();
      } finally {
        setLoading(false);
      }
    };
    fetchBookmarks();
  }, [getBookmarks]); // getBookmarks is included in the dependency array

  useEffect(() => {
    // Update state only when bookmarkList is updated
    setBookmarks(bookmarkList);
  }, [bookmarkList]);

  if (loading) {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          mt: 10,
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Container
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        padding: 2,
      }}
    >
      {bookmarks.length === 0 ? (
        <Typography variant="h6" color="text.secondary">
          찜 목록이 없습니다.
        </Typography>
      ) : (
        <Grid container spacing={2}>
          {bookmarks.map((bookmark) => (
            <Grid item xs={12} sm={6} key={bookmark.id}>
              <MyBookmarkItem bookmark={bookmark} />
            </Grid>
          ))}
        </Grid>
      )}
    </Container>
  );
}

export default MyBookmarkList;
