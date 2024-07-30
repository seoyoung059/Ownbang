import * as React from "react";
import { DemoContainer, DemoItem } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DateCalendar } from "@mui/x-date-pickers/DateCalendar";
import dayjs from "dayjs";
import { useTheme } from "@mui/material";

export default function ReservationCalendar({ value, onChange }) {
  const theme = useTheme();

  const shouldDisableDate = (date) => {
    return date.isBefore(dayjs(), "day"); // 오늘 이전 날짜 비활성화
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <DemoContainer
        components={["DateCalendar"]}
        sx={{ justifyContent: "center" }}
      >
        <DemoItem>
          <DateCalendar
            value={value}
            onChange={onChange}
            shouldDisableDate={shouldDisableDate}
          />
        </DemoItem>
      </DemoContainer>
    </LocalizationProvider>
  );
}
