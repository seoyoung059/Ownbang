import React, { useEffect } from "react";
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
import { useBoundStore } from "../../store/store";

function MyReservationList() {
  const theme = useTheme();

  const { reservationAll, getReservationList } = useBoundStore((state) => ({
    reservationAll: state.reservationAll,
    getReservationList: state.getReservationList,
  }));
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  const reservations = [];

  useEffect(() => {
    // 호출 후 예약 목록을 콘솔에 찍기
    const fetchData = async () => {
      try {
        await getReservationList();
        console.log("예약 목록:", reservationAll);
      } catch (error) {
        console.error("Error fetching reservation list:", error);
      }
    };

    fetchData();
  }, [getReservationList, reservationAll]);

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
