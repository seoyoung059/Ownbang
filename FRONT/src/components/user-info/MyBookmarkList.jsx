import React from "react";
import { Container, Typography } from "@mui/material";
import MyBookmarkItem from "./MyBookmarkItem";

const bookmarks = [
  {
    id: 1,
    image:
      "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA2MjBfMTIw%2FMDAxNjg3MjM1OTUzMzIx.6hYyuLzpp6TwxEPnpY__HAsPBtHwAi6yM3xEAxsFk9gg.y3G5PTrURXYYn9aO0EEQZ_cmS5fxRDDXaa0OFCcZwv4g.PNG.rimi_rimi%2Fimage.png&type=a340",
    title: "월세 120/120",
    size: "23.48m²",
    floor: "3층",
    location: "강남구 역삼동",
    description: "VV풀옵션 최다매물보유VV 모던 인...",
  },
  {
    id: 2,
    image:
      "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMjA1MDlfMjM3%2FMDAxNjUyMDg4MzI0Njcw.ioVDDNSx7I-0lZMMPeO1KLBzbc_mkCNdzSj-aj2F0lEg.BJB6dNgmrg6mgbquO3l3EsaaB_BOZYcLf6i140GSMhsg.JPEG.star3qp%2F20220429%25A3%25DF090751.jpg&type=l340_165",
    title: "월세 70/70",
    size: "24.93m²",
    floor: "1층",
    location: "강남구 논현동",
    description: "깔끔한 컨디션과 사이즈 최상",
  },
];

function MyBookmarkList() {
  return (
    <Container
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}
    >
      {bookmarks.map((bookmark) => (
        <MyBookmarkItem key={bookmark.id} bookmark={bookmark} />
      ))}
    </Container>
  );
}

export default MyBookmarkList;
