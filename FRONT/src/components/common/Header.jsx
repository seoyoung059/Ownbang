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
  Avatar,
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
      fetchUser();
    }
  }, [isAuthenticated, fetchUser]);

  const theme = useTheme();
  const isSm = useMediaQuery(theme.breakpoints.down("sm"));
  const isMd = useMediaQuery(theme.breakpoints.down("md"));

  const [menuAnchorEl, setMenuAnchorEl] = useState(null);
  const [avatarMenuAnchorEl, setAvatarMenuAnchorEl] = useState(null);

  const handleMenuOpen = (event) => setMenuAnchorEl(event.currentTarget);
  const handleMenuClose = () => setMenuAnchorEl(null);

  const handleAvatarMenuOpen = (event) =>
    setAvatarMenuAnchorEl(event.currentTarget);
  const handleAvatarMenuClose = () => setAvatarMenuAnchorEl(null);

  const handleLogout = () => {
    logout();
    handleAvatarMenuClose();
  };

  const headerStyle = {
    position: "fixed",
    left: 0,
    top: 0,
    zIndex: 1200,
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
                  <IconButton onClick={handleAvatarMenuOpen}>
                    <Avatar
                      alt={user.name}
                      src={user.profileImageUrl}
                      sx={{ width: 40, height: 40 }}
                    />
                  </IconButton>
                </>
              ) : (
                <Button onClick={() => handleNavigation("/login")}>
                  로그인
                </Button>
              )}
              {/* 지도 검색 버튼이 중개인이고 화면이 md 이하일 때만 사라짐 */}
              {!user.isAgent || !isMd ? (
                <IconButton
                  color="inherit"
                  onClick={() => handleNavigation("/estate")}
                  title="지도 검색"
                >
                  <MapOutlined />
                </IconButton>
              ) : null}
              {isAuthenticated && (
                <IconButton color="inherit" onClick={handleMenuOpen}>
                  <MenuOutlined />
                </IconButton>
              )}
            </div>
          </Box>
        </Box>
      </Box>

      {/* Backdrop 추가 */}
      <Backdrop
        sx={{ backgroundColor: "transparent", zIndex: theme.zIndex.drawer - 1 }}
        open={Boolean(menuAnchorEl) || Boolean(avatarMenuAnchorEl)}
        onClick={() => {
          handleMenuClose();
          handleAvatarMenuClose();
        }}
      />

      {/* 아바타 클릭 시 나오는 메뉴 */}
      {isAuthenticated && (
        <Menu
          anchorEl={avatarMenuAnchorEl}
          open={Boolean(avatarMenuAnchorEl)}
          onClose={handleAvatarMenuClose}
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
          <MenuItem onClick={handleLogout}>로그아웃</MenuItem>
        </Menu>
      )}

      {/* 햄버거 메뉴 클릭 시 나오는 메뉴 */}
      {isAuthenticated && (
        <Menu
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
          {!user.isAgent && (
            <MenuItem onClick={() => handleNavigation("/mypage")}>
              정보 관리
            </MenuItem>
          )}
          <MenuItem onClick={() => handleNavigation("/user-edit")}>
            유저 수정
          </MenuItem>
          {user.isAgent && [
            <MenuItem key="office" onClick={() => handleNavigation("/agent")}>
              내 사무소
            </MenuItem>,
            <MenuItem
              key="register"
              onClick={() => handleNavigation("/estate-register")}
            >
              매물 등록
            </MenuItem>,
          ]}
        </Menu>
      )}
    </>
  );
};

export default Header;
