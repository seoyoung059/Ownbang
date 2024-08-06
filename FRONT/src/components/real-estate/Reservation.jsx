import React, { useState, useCallback } from "react";
import { Box, Button, Typography, Alert, Snackbar } from "@mui/material";
import ReservationCalendar from "./ReservationCalendar";
import ReservationClock from "./ReservationClock";
import { useTheme } from "@mui/material/styles";

const Reservation = ({ onClose, makeReservation, item }) => {
  const theme = useTheme();
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState(null);
  const [showAlert, setShowAlert] = useState(false);
  const [showSuccess, setShowSuccess] = useState(false); // 성공 메시지 상태

  const reservationTime =
    selectedDate && selectedTime
      ? `${selectedDate.format("YYYY-MM-DD")}T${selectedTime.format(
          "HH:mm:ss"
        )}.000Z`
      : "";

  // 날짜와 시간 선택 후 예약 처리
  const onDateCheck = useCallback(async () => {
    if (!selectedDate || !selectedTime) {
      setShowAlert(true);
      return;
    }
    setShowAlert(false);

    const reservationData = {
      roomId: item.id,
      reservationTime: reservationTime,
      status: "APPLYED",
    };

    console.log(reservationData);

    try {
      await makeReservation(reservationData);
      setShowSuccess(true);
      setTimeout(onClose, 2000); // Optionally close the reservation after a delay
    } catch (error) {
      console.error("Reservation failed:", error);
    }
  }, [selectedDate, selectedTime, makeReservation, reservationTime, onClose]);

  // 날짜 변경 핸들러
  const handleDateChange = (date) => {
    setSelectedDate(date);
    if (showAlert) {
      setShowAlert(false);
    }
  };

  // 시간 변경 핸들러
  const handleTimeChange = (time) => {
    setSelectedTime(time);
    if (showAlert) {
      setShowAlert(false);
    }
  };

  // 성공 메시지 닫기 핸들러
  const handleSuccessClose = () => {
    setShowSuccess(false);
  };

  return (
    <>
      <Box
        sx={{
          marginLeft: 5,
          marginTop: 3,
          display: "flex",
          flexDirection: "column",
          gap: 2,
        }}
      >
        <Typography variant="h5">화상통화 예약하기</Typography>
        <Typography>예약을 원하는 날짜를 선택해주세요.</Typography>
      </Box>
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <ReservationCalendar value={selectedDate} onChange={handleDateChange} />
      </Box>

      <Typography sx={{ marginLeft: 5 }}>
        예약을 원하는 시간을 선택해주세요.
      </Typography>
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          marginTop: 1,
        }}
      >
        <ReservationClock value={selectedTime} onChange={handleTimeChange} />
        <Typography
          sx={{
            marginTop: "20px",
            fontFamily: theme.typography.fontFamily,
            fontSize: theme.typography.fontSize,
          }}
        >
          30분 단위로 통화가 예약됩니다.
        </Typography>
      </Box>

      <Box sx={{ display: "flex", flexDirection: "column", marginTop: 3 }}>
        {showAlert && (
          <Alert severity="warning" sx={{ marginBottom: 2 }}>
            날짜 및 시간 선택이 필요합니다.
          </Alert>
        )}
        {showSuccess && (
          <Alert
            severity="success"
            onClose={handleSuccessClose}
            sx={{ marginBottom: 2 }}
          >
            예약이 완료되었습니다.
          </Alert>
        )}
        <Button
          variant="contained"
          onClick={onDateCheck}
          sx={{
            marginTop: "16px",
            borderRadius: "7px",
            padding: "13px 15px",
            backgroundColor: theme.palette.primary.dark,
            fontSize: theme.typography.fontSize,
          }}
        >
          예약하기
        </Button>
      </Box>
    </>
  );
};

export default React.memo(Reservation);
