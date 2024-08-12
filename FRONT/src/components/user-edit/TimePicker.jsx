import React, { useEffect } from "react";
import dayjs from "dayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { TimePicker as MuiTimePicker } from "@mui/x-date-pickers/TimePicker";
import { Box, Typography, TextField } from "@mui/material";
import { useTheme } from "@emotion/react";

export default function TimePicker({ handleInputChange, userInfo }) {
  const theme = useTheme();
  const [weekdayStart, setWeekdayStart] = React.useState(
    dayjs().hour(9).minute(0)
  );
  const [weekdayClose, setWeekdayClose] = React.useState(
    dayjs().hour(18).minute(0)
  );
  const [weekendStart, setWeekendStart] = React.useState(
    dayjs().hour(9).minute(0)
  );
  const [weekendClose, setWeekendClose] = React.useState(
    dayjs().hour(18).minute(0)
  );

  // userInfo가 변경될 때마다 초기값 설정
  useEffect(() => {
    if (userInfo) {
      setWeekdayStart(
        userInfo.weekdayStartTime
          ? dayjs(userInfo.weekdayStartTime, "HH:mm")
          : dayjs().hour(9).minute(0)
      );
      setWeekdayClose(
        userInfo.weekdayEndTime
          ? dayjs(userInfo.weekdayEndTime, "HH:mm")
          : dayjs().hour(18).minute(0)
      );
      setWeekendStart(
        userInfo.weekendStartTime
          ? dayjs(userInfo.weekendStartTime, "HH:mm")
          : dayjs().hour(9).minute(0)
      );
      setWeekendClose(
        userInfo.weekendEndTime
          ? dayjs(userInfo.weekendEndTime, "HH:mm")
          : dayjs().hour(18).minute(0)
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
