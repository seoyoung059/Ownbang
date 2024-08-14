import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  TableRow,
  TableCell,
  Avatar,
  Typography,
  Box,
  Button,
  Backdrop,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
} from "@mui/material";
import {
  CheckCircleOutlined,
  CancelOutlined,
  PendingOutlined,
  HistoryOutlined,
  FiberManualRecord,
} from "@mui/icons-material";
import { useMediaQuery, useTheme } from "@mui/material";

export default function AgentReservationItem({
  reservation,
  confirmReservation,
  denyReservation,
  enterVideoRoom,
  getAgentReservationList,
  onSelectItem,
}) {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));
  const navigate = useNavigate();

  const [openBackdrop, setOpenBackdrop] = useState(false);

  const userInfo = reservation.userReservationInfoResponse;

  if (!userInfo) {
    return null;
  }

  const formatPhoneNumber = (phoneNumber) => {
    if (phoneNumber.length === 11) {
      return `${phoneNumber.slice(0, 3)}-${phoneNumber.slice(
        3,
        7
      )}-${phoneNumber.slice(7)}`;
    } else if (phoneNumber.length === 10) {
      return `${phoneNumber.slice(0, 3)}-${phoneNumber.slice(
        3,
        6
      )}-${phoneNumber.slice(6)}`;
    }
    return phoneNumber;
  };

  const getStatusIconAndText = (status) => {
    switch (status) {
      case "CONFIRMED":
        return {
          icon: (
            <CheckCircleOutlined
              style={{ color: theme.palette.success.main }}
            />
          ),
          text: "예약 확정",
        };
      case "APPLYED":
        return {
          icon: (
            <PendingOutlined style={{ color: theme.palette.warning.main }} />
          ),
          text: "대기 중",
        };
      case "CANCELLED":
        return {
          icon: <CancelOutlined style={{ color: theme.palette.error.main }} />,
          text: "예약 거절",
        };
      case "COMPLETED":
        return {
          icon: (
            <HistoryOutlined style={{ color: theme.palette.primary.dark }} />
          ),
          text: "통화 완료",
        };
      case "ENCODING":
        return {
          icon: (
            <HistoryOutlined style={{ color: theme.palette.primary.dark }} />
          ),
          text: "통화 완료",
        };
      case "RECORDING":
        return {
          icon: (
            <FiberManualRecord style={{ color: theme.palette.error.main }} />
          ),
          text: "녹화 중",
        };
      default:
        return {
          icon: null,
          text: "알 수 없음",
        };
    }
  };

  const { icon, text } = getStatusIconAndText(reservation.status);

  let reservationDate = new Date(
    reservation.reservationTime
  ).toLocaleDateString("ko-KR");
  if (reservationDate.endsWith(".")) {
    reservationDate = reservationDate.slice(0, -1);
  }

  const reservationTime = new Date(
    reservation.reservationTime
  ).toLocaleTimeString("ko-KR", {
    hour: "2-digit",
    minute: "2-digit",
  });

  const onConfirm = async () => {
    try {
      await confirmReservation(reservation.id);
      getAgentReservationList();
    } catch (error) {
      console.error("Failed to confirm reservation:", error);
    }
  };

  const onDeny = async () => {
    try {
      await denyReservation(reservation.id);
      getAgentReservationList();
    } catch (error) {
      console.error("Failed to deny reservation:", error);
    }
  };

  const onStartVideo = async () => {
    if (isMobile) {
      setOpenBackdrop(true);
    } else {
      navigate(`/video-chat/${reservation.id}`, {
        state: { reservationId: reservation.id },
      });
    }
  };

  const handleConfirm = () => {
    setOpenBackdrop(false);
    navigate(`/video-chat/${reservation.id}`, {
      state: { reservationId: reservation.id },
    });
  };

  const handleCloseBackdrop = () => {
    setOpenBackdrop(false);
  };

  const onViewDetail = () => {
    if (onSelectItem) {
      onSelectItem(reservation);
    }
  };

  return (
    <>
      <TableRow>
        {!isMobile && (
          <TableCell sx={{ padding: "4px 8px" }}>
            <Avatar
              src={reservation.roomProfileImage}
              variant="rounded"
              sx={{ width: "150px", height: "90px", cursor: "pointer" }}
              onClick={onViewDetail}
            />
          </TableCell>
        )}
        <TableCell>
          <Typography variant={isMobile ? "body2" : "body1"}>
            {reservationDate}
          </Typography>
        </TableCell>
        <TableCell>
          <Typography variant={isMobile ? "body2" : "body1"}>
            {reservationTime}
          </Typography>
        </TableCell>
        {!isMobile && (
          <TableCell>
            <Typography variant={"body1"}>{userInfo.userName}</Typography>
          </TableCell>
        )}
        <TableCell>
          <Box sx={{ display: "flex", alignItems: "center" }}>
            {icon}
            <Typography sx={{ ml: 1, whiteSpace: "nowrap" }} variant={"body1"}>
              {text}
            </Typography>
          </Box>
        </TableCell>
        <TableCell>
          <Button
            color="primary"
            onClick={onConfirm}
            disabled={reservation.status !== "APPLYED"}
            sx={{
              px: 0,
              py: 0,
              minWidth: "auto",
              fontSize: "0.875rem",
              color: theme.palette.success.main,
            }}
          >
            확정
          </Button>
          {" / "}
          <Button
            color="primary"
            onClick={onDeny}
            disabled={reservation.status !== "APPLYED"}
            sx={{
              px: 0,
              py: 0,
              minWidth: "auto",
              fontSize: "0.875rem",
              color: theme.palette.warning.main,
            }}
          >
            거절
          </Button>
        </TableCell>
        <TableCell>
          <Typography variant={"body1"}>
            {formatPhoneNumber(userInfo.phoneNumber)}
          </Typography>
        </TableCell>
        <TableCell>
          {reservation.status !== "COMPLETED" && (
            <Button
              variant="contained"
              sx={{
                backgroundColor: theme.palette.success.main,
                "&:hover": {
                  backgroundColor: theme.palette.success.main,
                },
              }}
              disabled={!reservation.enstance}
              onClick={onStartVideo}
            >
              입장하기
            </Button>
          )}
        </TableCell>
      </TableRow>
      <Backdrop open={openBackdrop} sx={{ zIndex: theme.zIndex.drawer + 1 }}>
        <Dialog open={openBackdrop} onClose={handleCloseBackdrop}>
          <DialogTitle>입장 알림</DialogTitle>
          <DialogContent>
            <DialogContentText>
              화상 화면은 가로 모드로 입장해주세요.
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseBackdrop} color="secondary">
              취소
            </Button>
            <Button onClick={handleConfirm} color="primary">
              확인
            </Button>
          </DialogActions>
        </Dialog>
      </Backdrop>
    </>
  );
}
