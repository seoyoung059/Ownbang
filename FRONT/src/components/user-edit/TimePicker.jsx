import React, { useEffect } from "react";
import dayjs from "dayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { TimePicker as MuiTimePicker } from "@mui/x-date-pickers/TimePicker";
import { Box, Typography, TextField } from "@mui/material";
import { useTheme } from "@emotion/react";

export default function TimePicker({ handleInputChange, userInfo }) {
  const theme = useTheme();

  // 기본값을 09:00 ~ 18:00으로 설정
  const defaultStartTime = dayjs().hour(9).minute(0);
  const defaultEndTime = dayjs().hour(18).minute(0);

  const [weekdayStart, setWeekdayStart] = React.useState(defaultStartTime);
  const [weekdayClose, setWeekdayClose] = React.useState(defaultEndTime);
  const [weekendStart, setWeekendStart] = React.useState(defaultStartTime);
  const [weekendClose, setWeekendClose] = React.useState(defaultEndTime);

  useEffect(() => {
    if (userInfo) {
      setWeekdayStart(
        userInfo.weekdayStartTime
          ? dayjs(userInfo.weekdayStartTime, "HH:mm")
          : defaultStartTime
      );
      setWeekdayClose(
        userInfo.weekdayEndTime
          ? dayjs(userInfo.weekdayEndTime, "HH:mm")
          : defaultEndTime
      );
      setWeekendStart(
        userInfo.weekendStartTime
          ? dayjs(userInfo.weekendStartTime, "HH:mm")
          : defaultStartTime
      );
      setWeekendClose(
        userInfo.weekendEndTime
          ? dayjs(userInfo.weekendEndTime, "HH:mm")
          : defaultEndTime
      );
    }
  }, [userInfo]);

  return (
    <LocalizationProvider
      dateAdapter={AdapterDayjs}
      localeText={{
        timePickerToolbarTitle: "시간 선택",
        okButtonLabel: "확인",
        cancelButtonLabel: "취소",
      }}
    >
      <Box sx={{ display: "flex", flexDirection: "column", gap: 3 }}>
        {/* 평일 */}
        <Box sx={{ display: "flex", gap: 2, alignItems: "center" }}>
          <Typography sx={{ color: theme.palette.text.secondary }}>
            평일
          </Typography>
          <MuiTimePicker
            label="영업 시작시간"
            value={weekdayStart}
            onChange={(newValue) => {
              setWeekdayStart(newValue);
              handleInputChange({
                target: {
                  name: "weekdayStartTime",
                  value: newValue.format("HH:mm"),
                },
              });
            }}
            textField={(params) => <TextField {...params} />}
            ampm={false} // 24시간 포맷 사용
          />
          <MuiTimePicker
            label="영업 종료시간"
            value={weekdayClose}
            onChange={(newValue) => {
              setWeekdayClose(newValue);
              handleInputChange({
                target: {
                  name: "weekdayEndTime",
                  value: newValue.format("HH:mm"),
                },
              });
            }}
            textField={(params) => <TextField {...params} />}
            ampm={false} // 24시간 포맷 사용
          />
        </Box>

        {/* 주말 */}
        <Box sx={{ display: "flex", gap: 2, alignItems: "center" }}>
          <Typography sx={{ color: theme.palette.text.secondary }}>
            주말
          </Typography>
          <MuiTimePicker
            label="영업 시작시간"
            value={weekendStart}
            onChange={(newValue) => {
              setWeekendStart(newValue);
              handleInputChange({
                target: {
                  name: "weekendStartTime",
                  value: newValue.format("HH:mm"),
                },
              });
            }}
            textField={(params) => <TextField {...params} />}
            ampm={false} // 24시간 포맷 사용
          />
          <MuiTimePicker
            label="영업 종료시간"
            value={weekendClose}
            onChange={(newValue) => {
              setWeekendClose(newValue);
              handleInputChange({
                target: {
                  name: "weekendEndTime",
                  value: newValue.format("HH:mm"),
                },
              });
            }}
            textField={(params) => <TextField {...params} />}
            ampm={false} // 24시간 포맷 사용
          />
        </Box>
      </Box>
    </LocalizationProvider>
  );
}
