import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import Video from "../components/video-chat/Video";
import CheckList from "../components/checklist/CheckList";
import { useBoundStore } from "../store/store";
import {
  Container,
  Grid,
  Box,
  Button,
  Divider,
  useMediaQuery,
  Typography,
  useTheme, // 이 부분 추가
} from "@mui/material";

export default function VideoChatPage() {
  useEffect(() => {
    const handleBeforeUnload = (event) => {
      event.preventDefault();
      event.returnValue = "";
    };

    const handleKeyDown = (event) => {
      if (
        event.key === "F5" ||
        (event.ctrlKey && event.key.toLowerCase() === "r") ||
        (event.metaKey && event.key.toLowerCase() === "r")
      ) {
        event.preventDefault();
        event.stopPropagation();
      }
    };

    window.addEventListener("keydown", handleKeyDown);
    window.addEventListener("beforeunload", handleBeforeUnload);

    return () => {
      window.removeEventListener("keydown", handleKeyDown);
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, []);

  const { enterVideoRoom, leaveVideoRoom, fetchUser, user } = useBoundStore(
    (state) => ({
      enterVideoRoom: state.enterVideoRoom,
      leaveVideoRoom: state.leaveVideoRoom,
      fetchUser: state.fetchUser,
      user: state.user,
    })
  );

  const location = useLocation();
  const reservationId = location.state?.reservationId;

  useEffect(() => {
    const fetchData = async () => {
      await fetchUser();
    };
    fetchData();
    console.log(location.state);
  }, []);

  const navigate = useNavigate();
  const theme = useTheme();
  const isSmallScreen = useMediaQuery("(max-width:600px)");
  const isMdScreen = useMediaQuery(theme.breakpoints.up("md"));

  const handleLeaveSession = () => {
    navigate("/");
  };

  return (
    <Container sx={{ mt: 12 }}>
      {!user.isAgent ? (
        <Grid
          container
          spacing={2}
          direction={isSmallScreen ? "column" : "row"}
        >
          <Grid item xs={12} md={8}>
            <Video
              enterVideoRoom={enterVideoRoom}
              leaveVideoRoom={leaveVideoRoom}
              user={user}
              fetchUser={fetchUser}
              reservationId={reservationId}
            />
          </Grid>
          <Grid item xs={12} md={4} sx={{ mt: isMdScreen ? 6 : 8 }}>
            <CheckList reservationId={reservationId} canEdit={true} />
          </Grid>
        </Grid>
      ) : (
        <Grid container spacing={2} direction="column">
          <Grid item xs={12}>
            <Video
              enterVideoRoom={enterVideoRoom}
              leaveVideoRoom={leaveVideoRoom}
              user={user}
              fetchUser={fetchUser}
              reservationId={reservationId}
            />
          </Grid>
        </Grid>
      )}
    </Container>
  );
}
