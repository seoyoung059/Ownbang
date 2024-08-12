import React, { useState, useEffect } from "react";
import { useBoundStore } from "../../store/store";
import {
  Box,
  Button,
  Typography,
  IconButton,
  Menu,
  MenuItem,
  Backdrop,
} from "@mui/material";
import { MenuOutlined, MapOutlined } from "@mui/icons-material";
import { useTheme, useMediaQuery } from "@mui/material";
import { useNavigate } from "react-router-dom";

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
  }, [isAuthenticated, fetchUser]);

  const theme = useTheme();
  const isSm = useMediaQuery(theme.breakpoints.down("sm"));

  const [menuAnchorEl, setMenuAnchorEl] = useState(null);

  const handleMenuOpen = (event) => {
    if (isAuthenticated) {
      setMenuAnchorEl(event.currentTarget);
    }
  };

  const handleMenuClose = () => setMenuAnchorEl(null);

  const handleLogout = () => {
    logout();
    handleMenuClose();
  };

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
    handleMenuClose();
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
                <>
                  <Button onClick={handleLogout}>로그아웃</Button>
                  <IconButton
                    color="inherit"
                    onClick={() => handleNavigation("/estate")}
                    title="지도 검색"
                  >
                    <MapOutlined />
                  </IconButton>
                </>
              ) : (
                <>
                  <Button onClick={() => handleNavigation("/login")}>
                    로그인
                  </Button>
                  <IconButton
                    color="inherit"
                    onClick={() => handleNavigation("/estate")}
                    title="지도 검색"
                  >
                    <MapOutlined />
                  </IconButton>
                </>
              )}
              <IconButton color="inherit" onClick={handleMenuOpen}>
                <MenuOutlined />
              </IconButton>
            </div>
          </Box>
        </Box>
      </Box>

      {/* Backdrop 추가 */}
      <Backdrop
        sx={{
          backgroundColor: "transparent",
          color: "#fff",
          zIndex: theme.zIndex.drawer - 1,
        }}
        open={Boolean(menuAnchorEl)}
        onClick={handleMenuClose}
      />

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
          horizontal: "center",
        }}
        disableScrollLock={true}
      >
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
