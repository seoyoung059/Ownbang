import React, { useEffect } from "react";
import { Container, Typography } from "@mui/material";
import MyBookmarkItem from "./MyBookmarkItem";
import { useBoundStore } from "../../store/store";

function MyBookmarkList() {
  const { bookmarkList, getBookmarks } = useBoundStore((state) => ({
    bookmarkList: state.bookmarkList,
    getBookmarks: state.getBookmarks,
  }));

  useEffect(() => {
    getBookmarks();
  }, [getBookmarks]);

  console.log(bookmarkList);
  return (
    <Container
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        padding: 2,
      }}
    >
      {bookmarkList.length === 0 ? (
        <Typography variant="h6" color="text.secondary">
          No bookmarks found.
        </Typography>
      ) : (
        bookmarkList.map((bookmark) => (
          <MyBookmarkItem key={bookmark.id} bookmark={bookmark} />
        ))
      )}
    </Container>
  );
}

export default MyBookmarkList;
