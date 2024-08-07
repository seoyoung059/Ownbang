import React, { useState, useEffect } from "react";
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
import { useNavigate } from "react-router-dom";
import Notification from "./Notification";

const Header = () => {
  const navigate = useNavigate();
  const { isAuthenticated, logout, user, fetchUser } = useBoundStore(
    (state) => ({
      isAuthenticated: state.isAuthenticated,
      logout: state.logout,
      user: state.user,
      fetchUser: state.fetchUser,
    })
  );

  useEffect(() => {
    if (isAuthenticated) {
      const fetchData = async () => {
        await fetchUser();
      };
      fetchData();
    }
  }, [fetchUser]);

  const theme = useTheme();
  const isSm = useMediaQuery(theme.breakpoints.down("sm"));

  const [open, setOpen] = useState(false);
  const [menuAnchorEl, setMenuAnchorEl] = useState(null);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);
  const handleMenuOpen = (event) => setMenuAnchorEl(event.currentTarget);
  const handleMenuClose = () => setMenuAnchorEl(null);

  const notificationCount = 1;

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

  const handleNavigation = (path) => {
    navigate(path);
    handleMenuClose(); // 메뉴를 닫기 위해 추가
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
                <Button onClick={() => handleNavigation("/login")}>
                  로그인
                </Button>
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
        <MenuItem onClick={() => handleNavigation("/estate")}>
          지도 검색
        </MenuItem>
        {isAuthenticated && user && !user.isAgent && (
          <MenuItem onClick={() => handleNavigation("/mypage")}>
            정보 관리
          </MenuItem>
        )}
        {isAuthenticated && (
          <MenuItem onClick={() => handleNavigation("/user-edit")}>
            유저 수정
          </MenuItem>
        )}
        {isAuthenticated && user && user.isAgent && (
          <MenuItem onClick={() => handleNavigation("/agent")}>
            내 사무소
          </MenuItem>
        )}
        {isAuthenticated && user && user.isAgent && (
          <MenuItem onClick={() => handleNavigation("/estate-register")}>
            매물 등록
          </MenuItem>
        )}
      </Menu>
    </>
  );
};

export default Header;
