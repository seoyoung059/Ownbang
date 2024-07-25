import { Box, Button, Typography } from "@mui/material";
import React, { useState } from "react";
import ReservationCalendar from "./ReservationCalendar";
import ReservationClock from "./ReservationClock";
import { useTheme } from "@mui/material";
import dayjs from "dayjs";

const Reservation = () => {
  const theme = useTheme();
  const [selectedDate, setSelectedDate] = useState(dayjs());
  const [selectedTime, setSelectedTime] = useState(dayjs());

  const onDateCheck = () => {
    console.log(
      `${selectedDate.format("YYYY-MM-DD")}  ${selectedTime.format("HH:mm")}`
    );
  };

  return (
    <>
      <Box
        sx={{ display: "flex", flexDirection: "column", alignItems: "center" }}
      >
        <ReservationCalendar value={selectedDate} onChange={setSelectedDate} />
        <ReservationClock value={selectedTime} onChange={setSelectedTime} />
        <Typography
          sx={{
            marginTop: "20px",
            fontFamily: theme.plus,
            fontSize: theme.fontSize.small,
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

export default Reservation;
