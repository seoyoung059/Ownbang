import * as React from "react";
import { DemoContainer, DemoItem } from "@mui/x-date-pickers/internals/demo";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DigitalClock } from "@mui/x-date-pickers/DigitalClock";
import dayjs from "dayjs";
import { Box } from "@mui/material";

export default function ReservationClock({ value, onChange }) {
  const handleTimeChange = (newValue) => {
    onChange(dayjs(newValue));
  };

  const shouldDisableTime = (value, view) => {
    const hour = value.hour();
    if (view === "hours") {
      return hour < 9 || hour > 18; // Disable hours outside 9 AM - 6 PM
    }
    return false;
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <DemoContainer components={["DigitalClock"]}>
        <DemoItem>
          <Box sx={{ width: "300px" }}>
            <DigitalClock
              skipDisabled
              minTime={dayjs().hour(9).minute(0)}
              maxTime={dayjs().hour(19).minute(0)}
              shouldDisableTime={shouldDisableTime}
              timeStep={30}
              onChange={handleTimeChange}
            />
          </Box>
        </DemoItem>
      </DemoContainer>
    </LocalizationProvider>
  );
}
