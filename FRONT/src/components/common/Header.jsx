import * as React from "react";
import { useState } from "react";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import Modal from "@mui/material/Modal";
import IconButton from "@mui/material/IconButton";
import Badge from "@mui/material/Badge";
import { Notifications, MenuOutlined } from "@mui/icons-material";
import { useTheme, useMediaQuery } from "@mui/material";

const Notification = ({ open, handleClose }) => {
  const theme = useTheme();
  const modalStyle = {
    position: "absolute",
    top: "12%",
    right: "4%",
    zIndex: "100",
    width: 400,
    bgcolor: theme.palette.background.paper,
    borderRadius: "10px",
    boxShadow: 24,
    p: 4,
  };
  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={modalStyle}>
        <Typography id="modal-modal-title" variant="h6" component="h2">
          알림
        </Typography>
        <Typography id="modal-modal-description" sx={{ mt: 2 }}>
          알림 내용
        </Typography>
      </Box>
    </Modal>
  );
};

const Header = () => {
  const theme = useTheme();
  const isSm = useMediaQuery(theme.breakpoints.down("sm"));

  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const notificationCount = 1; // 알림 개수 설정

  const headerStyle = {
    position: "fixed",
    left: 0,
    top: 0,
    margin: "0 auto",
    zIndex: "100",
    width: "100%",
    height: "80px",
    backgroundColor: theme.palette.primary.dark,
    color: theme.palette.common.white,
  };

  const contentsStyle = {
    display: "flex",
    height: "100%",
    alignItems: "center",
    justifyContent: "space-between",
    margin: "0 40px",
  };

  const navigationStyle = {
    color: "white",
    "& div": {
      display: "flex",
      gap: isSm ? "8px" : "28px",
      "& button": {
        color: "white",
        backgroundColor: theme.palette.primary.dark,
        border: "none",
      },
    },
  };

  return (
    <>
      <Box sx={headerStyle}>
        <Box sx={contentsStyle}>
          <a href="/" style={{ textDecoration: "none", color: "inherit" }}>
            <Typography
              sx={{
                fontFamily: theme.title.fontFamily,
                fontSize: theme.title.fontSize,
              }}
            >
              온방
            </Typography>
          </a>
          <Box sx={navigationStyle}>
            <div>
              <Button>로그인</Button>
              <IconButton color="inherit" onClick={handleOpen}>
                <Badge badgeContent={notificationCount} color="error">
                  <Notifications />
                </Badge>
              </IconButton>
              <IconButton color="inherit">
                <MenuOutlined />
              </IconButton>
            </div>
          </Box>
        </Box>
      </Box>
      <Notification open={open} handleClose={handleClose} />
    </>
  );
};

export default Header;
