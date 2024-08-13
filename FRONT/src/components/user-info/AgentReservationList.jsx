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
import { useMediaQuery, useTheme } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import RealEstateDetail from "../real-estate/RealEstateDetail";
import { useBoundStore } from "../../store/store";

// Custom pagination actions component
const CustomPaginationActions = () => {
  return null; // Return null to hide the pagination controls
};

function AgentReservationList() {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  const {
    getAgentReservationList,
    agentReservations,
    confirmReservation,
    denyReservation,
    getRoom,
    room,
  } = useBoundStore((state) => ({
    getAgentReservationList: state.getAgentReservationList,
    agentReservations: state.agentReservations,
    confirmReservation: state.confirmReservation,
    denyReservation: state.denyReservation,
    getRoom: state.getRoom,
    room: state.room,
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
    await getRoom(item.roomId); // Fetch room details when an item is selected
  };

  const handleCloseDetail = () => {
    setSelectedItem(null);
    setShowDetail(false);
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

  return (
    <Box sx={{ width: "100%", height: "100%" }}>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              {!isMobile && <TableCell>Room Image</TableCell>}
              <TableCell>예약 날짜</TableCell>
              <TableCell>예약 시간</TableCell>
              {!isMobile && <TableCell>사용자 이름</TableCell>}
              <TableCell>상태</TableCell>
              <TableCell>Actions</TableCell>
              <TableCell>전화번호</TableCell>
              <TableCell>통화</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {agentReservations
              .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              .map((reservation) => (
                <AgentReservationItem
                  key={reservation.id}
                  reservation={reservation}
                  confirmReservation={confirmReservation}
                  denyReservation={denyReservation}
                  getAgentReservationList={getAgentReservationList}
                  onSelectItem={handleSelectItem} // Pass the callback
                />
              ))}
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
                ActionsComponent={CustomPaginationActions} // Use custom actions component
              />
            </TableRow>
          </TableFooter>
        </Table>
      </TableContainer>

      {showDetail && selectedItem && (
        <Box
          sx={{
            position: "fixed",
            top: 0,
            right: 0,
            width: "100%",
            height: "100%",
            backgroundColor: "rgba(0, 0, 0, 0.5)", // Slightly dark background
            zIndex: 1000,
          }}
          onClick={handleCloseDetail}
        >
          <Box
            sx={{
              position: "fixed",
              top: "10%",
              right: "10%",
              backgroundColor: theme.palette.background.default,
              padding: 3,
              borderRadius: 1,
              boxShadow: 3,
              width: "60%",
              height: "80%",
              overflow: "auto",
              zIndex: 1001,
            }}
            onClick={(e) => e.stopPropagation()}
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
              onOpenReservationCard={() => {}}
              isAuthenticated={true}
              user={{ isAgent: true }}
            />
          </Box>
        </Box>
      )}
    </Box>
  );
}

export default AgentReservationList;
