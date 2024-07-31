import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Video from "../components/video-chat/Video";
import CheckList from "../components/checklist/CheckList";
import { useBoundStore } from "../store/store";
import {
  Container,
  Grid,
  Box,
  Button,
  Divider,
  IconButton,
  useMediaQuery,
  Typography,
} from "@mui/material";

import { ExitToApp } from "@mui/icons-material";

const exampleData = {
  roomToken: "sampleToken",
  role: "host",
  nickname: "user123",
  leftUser: "userLeft",
  rightUser: "userRight",
  leftOpinion: "opinionLeft",
  rightOpinion: "opinionRight",
  phaseNum: 1,
  phaseDetail: 2,
  roomId: "412321",
};

export default function VideoChatPage() {
  const navigate = useNavigate();
  const [tabValue, setTabValue] = useState(0);
  const user = useBoundStore((state) => state.user);
  const isSmallScreen = useMediaQuery("(max-width:600px)");

  const handleChange = (newValue) => {
    setTabValue(newValue);
  };

  const handleLeaveSession = () => {
    navigate("/"); // 추가 경로 설정
  };

  return (
    <Container sx={{ mt: 8 }}>
      {!user.isAgent ? (
        <Grid
          container
          spacing={2}
          direction={isSmallScreen ? "column" : "row"}
        >
          <Grid item xs={12} md={8}>
            <Video data={exampleData} />
          </Grid>
          <Grid item xs={12} md={4}>
            <Box
              sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                mt: 4,
                mb: 2,
                gap: 2,
              }}
            >
              <Button
                variant={tabValue === 0 ? "contained" : "outlined"}
                onClick={() => handleChange(0)}
                sx={{ width: "40%" }}
              >
                채팅
              </Button>
              <Divider orientation="vertical" flexItem />
              <Button
                variant={tabValue === 1 ? "contained" : "outlined"}
                onClick={() => handleChange(1)}
                sx={{ width: "40%" }}
              >
                체크리스트
              </Button>
            </Box>
            {tabValue === 0 ? <div>dd</div> : <CheckList />}
          </Grid>
        </Grid>
      ) : (
        <Grid container spacing={2} direction="column">
          <Grid item xs={12}>
            <Video data={exampleData} />
          </Grid>
          <Grid item xs={12}>
            <Typography>채팅 컴퍼넌트</Typography>
          </Grid>
        </Grid>
      )}
    </Container>
  );
}
