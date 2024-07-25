import React from "react";
import {
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Paper,
} from "@mui/material";
import MyReservationItem from "./MyReservationItem";
import { useMediaQuery, useTheme } from "@mui/material";

const reservations = [
  {
    id: 1,
    image: "https://via.placeholder.com/150",
    date: "2024.06.26",
    time: "16:00 ~ 16:30",
    agent: "김준영 (행복덕방)",
    status: "확정",
    statusColor: "success",
  },
  {
    id: 2,
    image: "https://via.placeholder.com/150",
    date: "2024.06.27",
    time: "15:30 ~ 16:00",
    agent: "이소희 (복덩이)",
    status: "취소",
    statusColor: "error",
  },
  {
    id: 3,
    image: "https://via.placeholder.com/150",
    date: "2024.06.27",
    time: "16:00 ~ 16:30",
    agent: "김서영 (서영이네)",
    status: "대기",
    statusColor: "warning",
  },
];

function MyReservationList() {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            {!isMobile && <TableCell>미리보기</TableCell>}
            <TableCell>예약일</TableCell>
            <TableCell>예약 시간</TableCell>
            {!isMobile && <TableCell>공인중개사명</TableCell>}
            <TableCell>예약 상태</TableCell>
            <TableCell>화상 채팅</TableCell>
          </TableRow>
        </TableHead>
        <TableBody sx={{ bgcolor: theme.palette.common.white }}>
          {reservations.map((reservation) => (
            <MyReservationItem key={reservation.id} reservation={reservation} />
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

export default MyReservationList;
