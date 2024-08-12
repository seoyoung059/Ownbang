import React from "react";
import { Box, Button, Typography } from "@mui/material";
import dayjs from "dayjs";

export default function ReservationClock({ value, onChange, availableTimes }) {
  const handleTimeChange = (time) => {
    onChange(time ? dayjs(time, "HH:mm") : null);
  };

  return (
    <Box
      sx={{
        width: "300px",
        display: "flex",
        flexDirection: "row",
        justifyContent: "center",
      }}
    >
      {availableTimes.length === 0 ? (
        <Typography variant="body1" color="textSecondary">
          예약 가능한 시간이 없습니다.
        </Typography>
      ) : (
        <Box
          sx={{
            display: "flex",
            flexWrap: "wrap",
            gap: 1,
            width: "100%", // Adjust width to fit 3 buttons per row
          }}
        >
          {availableTimes.map((time, index) => (
            <Button
              key={index}
              variant={
                value && value.format("HH:mm") === time
                  ? "contained"
                  : "outlined"
              }
              onClick={() => handleTimeChange(time)}
              sx={{
                flex: "1 1 30%", // Adjust button size to fit 3 per row
                marginBottom: 1,
              }}
            >
              {time}
            </Button>
          ))}
        </Box>
      )}
    </Box>
  );
}
