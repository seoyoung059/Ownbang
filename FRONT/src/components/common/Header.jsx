import React, { useState } from "react";
import { useBoundStore } from "../../store/store";
import {
  Box,
  Button,
  Typography,
  IconButton,
  Badge,
  Menu,
  MenuItem,
} from "@mui/material";
import { Notifications, MenuOutlined } from "@mui/icons-material";
import { useTheme, useMediaQuery } from "@mui/material";

import Notification from "./Notification";

const Header = () => {
  const { isAuthenticated, logout, user } = useBoundStore((state) => ({
    isAuthenticated: state.isAuthenticated,
    logout: state.logout,
    user: state.user,
  }));
  const theme = useTheme();
  const isSm = useMediaQuery(theme.breakpoints.down("sm"));

  const [open, setOpen] = useState(false);
  const [menuAnchorEl, setMenuAnchorEl] = useState(null); // Menu anchor element
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);
  const handleMenuOpen = (event) => setMenuAnchorEl(event.currentTarget); // 메뉴 열기
  const handleMenuClose = () => setMenuAnchorEl(null); // 메뉴 닫기

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

  const handleEstate = () => {
    window.location.href = "/estate";
  };

  const handleMyinfo = () => {
    window.location.href = "/mypage";
  };

  const handleUserEdit = () => {
    window.location.href = "/user-edit";
  };

  const handleAgent = () => {
    window.location.href = "/agent";
  };

  const handleEstateRegister = () => {
    window.location.href = "/estate-register";
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
              {isAuthenticated ? (
                <Button onClick={logout}>로그아웃</Button>
              ) : (
                <Button onClick={handleLogin}>로그인</Button>
              )}

              <IconButton color="inherit" onClick={handleOpen}>
                <Badge badgeContent={notificationCount} color="error">
                  <Notifications />
                </Badge>
              </IconButton>
              <IconButton color="inherit" onClick={handleMenuOpen}>
                <MenuOutlined />
              </IconButton>
            </div>
          </Box>
        </Box>
      </Box>
      <Notification open={open} handleClose={handleClose} />

      <Menu
        sx={{ mt: 4 }}
        anchorEl={menuAnchorEl}
        open={Boolean(menuAnchorEl)}
        onClose={handleMenuClose}
        anchorOrigin={{
          vertical: "bottom",
          horizontal: "left",
        }}
        transformOrigin={{
          vertical: "top",
          horizontal: "left",
        }}
      >
        <MenuItem onClick={handleEstate}>지도 검색</MenuItem>
        {isAuthenticated && !user.isAgent && (
          <MenuItem onClick={handleMyinfo}>정보 관리</MenuItem>
        )}
        {isAuthenticated && (
          <MenuItem onClick={handleUserEdit}>유저 수정</MenuItem>
        )}
        {isAuthenticated && user.isAgent && (
          <MenuItem onClick={handleAgent}>내 사무소</MenuItem>
        )}
        {isAuthenticated && user.isAgent && (
          <MenuItem onClick={handleEstateRegister}>매물 등록</MenuItem>
        )}
      </Menu>
    </>
  );
};

export default Header;
