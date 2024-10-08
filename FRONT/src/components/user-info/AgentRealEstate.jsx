import React, { useEffect, useState } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  TableFooter,
  TablePagination,
  CircularProgress,
  Box,
} from "@mui/material";
import { useTheme, useMediaQuery } from "@mui/material";
import { useBoundStore } from "../../store/store";

const MyRealEstate = () => {
  const { myRooms, viewAllRooms } = useBoundStore((state) => ({
    myRooms: state.myRooms,
    viewAllRooms: state.viewAllRooms,
  }));

  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [loading, setLoading] = useState(true);

  const formatCurrency = (value) => {
    return value ? Number(value).toLocaleString() : "0";
  };

  useEffect(() => {
    const fetchRooms = async () => {
      setLoading(true);
      try {
        await viewAllRooms();
      } finally {
        setLoading(false);
      }
    };

    fetchRooms();
  }, [viewAllRooms]);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const emptyRows =
    page > 0 ? Math.max(0, (1 + page) * rowsPerPage - myRooms.length) : 0;

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

  return (
    <TableContainer component={Paper} sx={{ width: "80%", margin: "auto" }}>
      <Table sx={{ minWidth: 500 }}>
        <TableHead>
          <TableRow>
            {!isMobile && (
              <TableCell align="center" sx={{ minWidth: 90 }}>
                매물 사진
              </TableCell>
            )}
            <TableCell align="center" sx={{ minWidth: 240 }}>
              매물 주소지
            </TableCell>
            <TableCell align="center" sx={{ minWidth: 60 }}>
              방 종류
            </TableCell>
            <TableCell align="center" sx={{ minWidth: 60 }}>
              구조
            </TableCell>
            <TableCell align="center" sx={{ minWidth: 60 }}>
              거래 종류
            </TableCell>
            <TableCell align="center" sx={{ minWidth: 60 }}>
              전용 면적
            </TableCell>
            <TableCell align="center" sx={{ minWidth: 60 }}>
              공급 면적
            </TableCell>
            <TableCell align="center" sx={{ minWidth: 30 }}>
              층수
            </TableCell>
            <TableCell align="center" sx={{ minWidth: 50 }}>
              보증금
            </TableCell>
            <TableCell align="center" sx={{ minWidth: 30 }}>
              월세
            </TableCell>
            <TableCell align="center" sx={{ minWidth: 50 }}>
              관리비
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody sx={{ backgroundColor: theme.palette.background.default }}>
          {myRooms
            .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
            .map((row) => (
              <TableRow
                key={row.id}
                sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
              >
                {!isMobile && (
                  <TableCell align="center">
                    {row.profileImageUrl ? (
                      <img
                        src={row.profileImageUrl}
                        width="150px"
                        height="100px"
                        style={{ borderRadius: "5px" }}
                      />
                    ) : (
                      "No Image"
                    )}
                  </TableCell>
                )}

                <TableCell align="center">
                  {row.road ? row.road : "없음"}
                </TableCell>
                <TableCell align="center">
                  {row.roomType ? row.roomType : "없음"}
                </TableCell>
                <TableCell align="center">
                  {row.structure ? row.structure : "없음"}
                </TableCell>
                <TableCell align="center">
                  {row.dealType ? row.dealType : "없음"}
                </TableCell>
                <TableCell align="center">
                  {row.exclusiveArea ? row.exclusiveArea : "없음"}
                </TableCell>
                <TableCell align="center">
                  {row.supplyArea ? row.supplyArea : "없음"}
                </TableCell>
                <TableCell align="center">
                  {row.roomFloor ? row.roomFloor : "없음"}
                </TableCell>
                <TableCell align="center">
                  {row.deposit ? formatCurrency(row.deposit) : "없음"}
                </TableCell>
                <TableCell align="center">
                  {row.monthlyRent ? formatCurrency(row.monthlyRent) : "없음"}
                </TableCell>
                <TableCell align="center">
                  {row.maintenanceFee
                    ? formatCurrency(row.maintenanceFee)
                    : "N/A"}
                </TableCell>
              </TableRow>
            ))}
          {emptyRows > 0 && (
            <TableRow style={{ height: 53 * emptyRows }}>
              <TableCell colSpan={11} />
            </TableRow>
          )}
        </TableBody>
        <TableFooter>
          <TableRow>
            <TablePagination
              rowsPerPageOptions={[5, 10, 25]}
              count={myRooms.length}
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
};

export default MyRealEstate;
