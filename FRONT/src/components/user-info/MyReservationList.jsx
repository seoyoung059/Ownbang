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
  TableFooter,
  TablePagination,
  Box,
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
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  // Function to refresh reservations
  const refreshReservations = async () => {
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

  useEffect(() => {
    refreshReservations(); // Fetch reservations on component mount
  }, [getReservationList]);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  if (loading) {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          mt: 10,
          height: "100vh",
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return <Typography color="error">{error}</Typography>;
  }

  const emptyRows =
    page > 0 ? Math.max(0, (1 + page) * rowsPerPage - reservations.length) : 0;

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
        <TableBody sx={{ bgcolor: theme.palette.background.default }}>
          {reservations
            .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
            .map((reservation) => (
              <MyReservationItem
                key={reservation.id}
                reservation={reservation}
                refreshReservations={refreshReservations} // Pass the refresh function to each item
              />
            ))}
          {emptyRows > 0 && (
            <TableRow style={{ height: 53 * emptyRows }}>
              <TableCell colSpan={6} />
            </TableRow>
          )}
        </TableBody>
        <TableFooter>
          <TableRow>
            <TablePagination
              rowsPerPageOptions={[5, 10, 25]}
              count={reservations.length}
              rowsPerPage={rowsPerPage}
              page={page}
              onPageChange={handleChangePage}
              onRowsPerPageChange={handleChangeRowsPerPage}
              labelRowsPerPage="페이지당 행 수:"
              labelDisplayedRows={({ from, to, count }) =>
                `${from}-${to} / ${count !== -1 ? count : `more than ${to}`}`
              }
            />
          </TableRow>
        </TableFooter>
      </Table>
    </TableContainer>
  );
}

export default MyReservationList;
