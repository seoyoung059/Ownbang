import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Button,
  Typography,
  Modal,
  IconButton,
  Badge,
  Backdrop,
} from "@mui/material";

import { Notifications, MenuOutlined } from "@mui/icons-material";
import { useTheme, useMediaQuery } from "@mui/material";

import Notification from "./Notification";

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
    zIndex: "1200",
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

  const handleLogin = () => {
    window.location.href = "/login";
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
              <Button onClick={handleLogin}>로그인</Button>
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
