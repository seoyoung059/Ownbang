import React, { useEffect, useState } from "react";
import { Container, Typography } from "@mui/material";
import MyBookmarkItem from "./MyBookmarkItem";
import { useBoundStore } from "../../store/store";

function MyBookmarkList() {
  const { bookmarkList, getBookmarks } = useBoundStore((state) => ({
    bookmarkList: state.bookmarkList,
    getBookmarks: state.getBookmarks,
  }));

  const [bookmarks, setBookmarks] = useState(bookmarkList);

  useEffect(() => {
    const fetchBookmarks = async () => {
      await getBookmarks();
    };
    fetchBookmarks();
  }, [getBookmarks]); // getBookmarks는 의존성 배열에 포함

  useEffect(() => {
    // bookmarkList가 업데이트될 때만 상태 업데이트
    setBookmarks(bookmarkList);
  }, [bookmarkList]);

  // 콘솔 로그는 상태 업데이트 후에만 출력
  useEffect(() => {
    console.log(bookmarks);
  }, [bookmarks]);

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
        bookmarks.map((bookmark) => (
          <MyBookmarkItem key={bookmark.id} bookmark={bookmark} />
        ))
      )}
    </Container>
  );
}

export default MyBookmarkList;
