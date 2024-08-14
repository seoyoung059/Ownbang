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
  IconButton,
} from "@mui/material";
import AgentReservationItem from "./AgentReservationItem";
import CloseIcon from "@mui/icons-material/Close";
import RealEstateDetail from "../real-estate/RealEstateDetail";
import Reservation from "../real-estate/Reservation";
import { useTheme, useMediaQuery } from "@mui/material";
import { useBoundStore } from "../../store/store";

const AgentReservationList = () => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  const {
    getAgentReservationList,
    agentReservations,
    confirmReservation,
    denyReservation,
    getRoom,
    room,
    makeReservation,
    getAgentAvailable,
  } = useBoundStore((state) => ({
    getAgentReservationList: state.getAgentReservationList,
    agentReservations: state.agentReservations,
    confirmReservation: state.confirmReservation,
    denyReservation: state.denyReservation,
    getRoom: state.getRoom,
    room: state.room,
    makeReservation: state.makeReservation,
    getAgentAvailable: state.getAgentAvailable,
  }));

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [selectedItem, setSelectedItem] = useState(null);
  const [showDetail, setShowDetail] = useState(false);

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

  const handleSelectItem = async (item) => {
    setSelectedItem(item);
    setShowDetail(true);
    await getRoom(item.roomId);
  };

  const handleCloseDetail = () => {
    setSelectedItem(null);
    setShowDetail(false);
    setShowReservation(false);
  };

  const handleOpenReservationCard = () => {
    setShowReservation(true);
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
    return (
      <Typography variant="h6" color="error">
        {error}
      </Typography>
    );
  }

  const emptyRows =
    page > 0
      ? Math.max(0, (1 + page) * rowsPerPage - agentReservations.length)
      : 0;

  return (
    <Box>
      <Box sx={{ width: "100%", height: "100%" }}>
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                {!isMobile && (
                  <TableCell sx={{ minWidth: 150 }}>매물 사진</TableCell>
                )}
                <TableCell sx={{ minWidth: 100 }}>예약 날짜</TableCell>
                <TableCell sx={{ minWidth: 90 }}>예약 시간</TableCell>
                {!isMobile && (
                  <TableCell sx={{ minWidth: 80 }}>예약자명</TableCell>
                )}
                <TableCell sx={{ minWidth: 100 }}>예약 상태</TableCell>
                <TableCell sx={{ minWidth: 80 }}>상태 변경</TableCell>
                <TableCell sx={{ minWidth: 130 }}>전화번호</TableCell>
                <TableCell sx={{ minWidth: 100 }}>통화</TableCell>
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
                    onSelectItem={handleSelectItem}
                  />
                ))}
              {emptyRows > 0 && (
                <TableRow style={{ height: `calc(53px * ${emptyRows})` }}>
                  <TableCell colSpan={8} />
                </TableRow>
              )}
            </TableBody>
            <TableFooter>
              <TableRow>
                <TablePagination
                  rowsPerPageOptions={[10, 25, 50]}
                  count={agentReservations.length}
                  rowsPerPage={rowsPerPage}
                  page={page}
                  onPageChange={handleChangePage}
                  onRowsPerPageChange={handleChangeRowsPerPage}
                />
              </TableRow>
            </TableFooter>
          </Table>
        </TableContainer>
      </Box>
      {selectedItem && (
        <Box
          sx={{
            position: "fixed",
            top: "55%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            backgroundColor: theme.palette.background.default,
            padding: 3,
            borderRadius: 1,
            boxShadow: 3,
            width: "800px",
            height: "80%",
            overflow: "auto",
            zIndex: 50,
          }}
        >
          <IconButton
            onClick={handleCloseDetail}
            sx={{
              position: "absolute",
              top: 8,
              right: 8,
            }}
          >
            <CloseIcon />
          </IconButton>
          <RealEstateDetail
            item={room}
            onOpenReservationCard={handleOpenReservationCard}
            isAuthenticated={true}
            user={{ isAgent: true }}
          />
        </Box>
      )}
    </Box>
  );
};

export default AgentReservationList;
