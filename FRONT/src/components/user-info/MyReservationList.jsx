import React, { useState, useEffect } from "react";
import {
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Paper,
  CircularProgress,
  Typography,
} from "@mui/material";
import MyReservationItem from "./MyReservationItem";
import { useMediaQuery, useTheme } from "@mui/material";
import { useBoundStore } from "../../store/store";

function MyReservationList() {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  const { getReservationList, reservations } = useBoundStore((state) => ({
    getReservationList: state.getReservationList,
    reservations: state.reservations,
  }));

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchReservations = async () => {
      setLoading(true);
      setError(null);
      try {
        await getReservationList();
      } catch (error) {
        setError("Failed to fetch reservations. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchReservations();
  }, [getReservationList]);

  if (loading) {
    return <CircularProgress />;
  }

  if (error) {
    return <Typography color="error">{error}</Typography>;
  }

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
