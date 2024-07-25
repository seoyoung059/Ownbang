import * as React from "react";
import dayjs from "dayjs";
import { DemoContainer, DemoItem } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DateCalendar } from "@mui/x-date-pickers/DateCalendar";
import { Box } from "@mui/material";
import { useTheme } from "@mui/material";

export default function ReservationCalendar({ value, onChange }) {
  const theme = useTheme();

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <DemoContainer
        components={["DateCalendar"]}
        sx={{ justifyContent: "center" }}
      >
        <DemoItem>
          <DateCalendar value={value} onChange={onChange} />
        </DemoItem>
      </DemoContainer>
    </LocalizationProvider>
  );
}
