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
  Box,
  TableFooter,
  TablePagination,
} from "@mui/material";
import AgentReservationItem from "./AgentReservationItem";
import { useMediaQuery, useTheme } from "@mui/material";
import { useBoundStore } from "../../store/store";

function AgentReservationList() {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  const {
    getAgentReservationList,
    agentReservations,
    confirmReservation,
    denyReservation,
  } = useBoundStore((state) => ({
    getAgentReservationList: state.getAgentReservationList,
    agentReservations: state.agentReservations,
    confirmReservation: state.confirmReservation,
    denyReservation: state.denyReservation,
  }));

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  useEffect(() => {
    const fetchReservations = async () => {
      setLoading(true);
      setError(null);
      try {
        await getAgentReservationList();
      } catch (error) {
        setError("Failed to fetch reservations. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchReservations();
  }, [getAgentReservationList]);

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
    page > 0
      ? Math.max(0, (1 + page) * rowsPerPage - agentReservations.length)
      : 0;

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            {!isMobile && (
              <TableCell align="center" sx={{ minWidth: 150 }}>
                미리보기
              </TableCell>
            )}
            <TableCell align="center" sx={{ minWidth: 100 }}>
              예약일
            </TableCell>
            <TableCell align="center" sx={{ minWidth: 100 }}>
              예약 시간
            </TableCell>
            {!isMobile && (
              <TableCell align="center" sx={{ minWidth: 60 }}>
                예약자명
              </TableCell>
            )}
            <TableCell align="center" sx={{ minWidth: 110 }}>
              예약 상태
            </TableCell>
            <TableCell align="center" sx={{ minWidth: 100 }}>
              예약 상태 변경
            </TableCell>
            <TableCell align="center" sx={{ minWidth: 140 }}>
              예약자 번호
            </TableCell>
            <TableCell align="center" sx={{ minWidth: 100 }}>
              화상 채팅
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody sx={{ bgcolor: theme.palette.background.default }}>
          {agentReservations
            .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
            .map((reservation) => (
              <AgentReservationItem
                key={reservation.id}
                reservation={reservation}
                confirmReservation={confirmReservation}
                denyReservation={denyReservation}
                getAgentReservationList={getAgentReservationList}
              />
            ))}
          {emptyRows > 0 && (
            <TableRow style={{ height: 53 * emptyRows }}>
              <TableCell
                colSpan={isMobile ? 6 : 8}
                sx={{ bgcolor: theme.palette.background.default }}
              />
            </TableRow>
          )}
        </TableBody>
        <TableFooter>
          <TableRow>
            <TablePagination
              rowsPerPageOptions={[5, 10, 25]}
              count={agentReservations.length}
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

export default AgentReservationList;
