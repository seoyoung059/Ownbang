import React, { useState, useCallback } from "react";
import { Box, Button, Typography } from "@mui/material";
import ReservationCalendar from "./ReservationCalendar";
import ReservationClock from "./ReservationClock";
import { useTheme } from "@mui/material/styles";
import dayjs from "dayjs";

const Reservation = ({ onClose }) => {
  const theme = useTheme();
  const [selectedDate, setSelectedDate] = useState(dayjs());
  const [selectedTime, setSelectedTime] = useState(dayjs());

  const onDateCheck = useCallback(() => {
    console.log(
      `${selectedDate.format("YYYY-MM-DD")}  ${selectedTime.format("HH:mm")}`
    );
    onClose();
  }, [selectedDate, selectedTime, onClose]);

  return (
    <>
      <Typography sx={{ marginLeft: 5, marginTop: 2 }}>
        예약을 원하는 날짜를 선택해주세요.
      </Typography>
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <ReservationCalendar value={selectedDate} onChange={setSelectedDate} />
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
        <ReservationClock value={selectedTime} onChange={setSelectedTime} />
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

      <Box sx={{ display: "flex", flexDirection: "column" }}>
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
