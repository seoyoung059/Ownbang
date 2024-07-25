import React from "react";
import {
  TableRow,
  TableCell,
  Avatar,
  Typography,
  Button,
  Box,
} from "@mui/material";
import {
  CheckCircleOutlined,
  CancelOutlined,
  PendingOutlined,
} from "@mui/icons-material";
import { useMediaQuery, useTheme } from "@mui/material";

export default function MyReservationItem({ reservation }) {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  const getStatusIcon = (status) => {
    switch (status) {
      case "확정":
        return <CheckCircleOutlined style={{ color: "green" }} />;
      case "취소":
        return <CancelOutlined style={{ color: "red" }} />;
      case "대기":
        return <PendingOutlined style={{ color: "orange" }} />;
      default:
        return null;
    }
  };

  return (
    <TableRow>
      {!isMobile && (
        <TableCell>
          <Avatar src={reservation.image} variant="rounded" />
        </TableCell>
      )}
      <TableCell>
        <Typography variant={isMobile ? "body2" : "body1"}>
          {reservation.date}
        </Typography>
      </TableCell>
      <TableCell>
        <Typography variant={isMobile ? "body2" : "body1"}>
          {reservation.time}
        </Typography>
      </TableCell>
      {!isMobile && (
        <TableCell>
          <Typography variant={"body1"}>{reservation.agent}</Typography>
        </TableCell>
      )}
      <TableCell>
        <Box sx={{ display: "flex", alignItems: "center" }}>
          {getStatusIcon(reservation.status)}
          <Typography sx={{ ml: 1, whiteSpace: "nowrap" }} variant={"body1"}>
            {reservation.status}
          </Typography>
        </Box>
      </TableCell>
      <TableCell>
        <Button
          variant="contained"
          disabled={reservation.status !== "확정"}
          size={isMobile ? "small" : "medium"}
        >
          입장
        </Button>
      </TableCell>
    </TableRow>
  );
}
