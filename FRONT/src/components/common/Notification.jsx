import * as React from "react";
import { Box, Typography, Backdrop, useTheme } from "@mui/material";

export default Notification = ({ open, handleClose }) => {
  const theme = useTheme();
  const modalStyle = {
    position: "fixed",
    top: "12%",
    right: "4%",
    zIndex: "1300",
    width: 400,
    bgcolor: theme.palette.background.paper,
    borderRadius: "10px",
    boxShadow: 24,
    p: 4,
  };
  return (
    <Backdrop
      open={open}
      sx={{ zIndex: theme.zIndex.modal + 1 }}
      onClick={handleClose}
    >
      <Box sx={modalStyle} onClick={(e) => e.stopPropagation()}>
        <Typography id="modal-modal-title" variant="h6" component="h2">
          알림
        </Typography>
        <Typography id="modal-modal-description" sx={{ mt: 2 }}>
          알림 내용
        </Typography>
      </Box>
    </Backdrop>
  );
};
