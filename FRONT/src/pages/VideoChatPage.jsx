import React, { useState } from "react";
import Video from "../components/video-chat/Video";
import CheckList from "../components/checklist/CheckList";

import { Container, Grid, Box, Button, Divider } from "@mui/material";

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
  const [tabValue, setTabValue] = useState(0);

  const handleChange = (newValue) => {
    setTabValue(newValue);
  };
  return (
    <Container sx={{ mt: 16 }}>
      <Grid container spacing={2}>
        <Grid item xs={8}>
          <Video data={exampleData} />
        </Grid>
        <Grid item xs={4}>
          <Box
            sx={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              mb: 2,
              gap: 2,
            }}
          >
            <Button
              variant={tabValue === 0 ? "contained" : "outlined"}
              onClick={() => handleChange(0)}
              sx={{ width: "40%" }}
            >
              체크리스트
            </Button>
            <Divider orientation="vertical" flexItem />
            <Button
              variant={tabValue === 1 ? "contained" : "outlined"}
              onClick={() => handleChange(1)}
              sx={{ width: "40%" }}
            >
              채팅
            </Button>
          </Box>
          {tabValue === 0 ? <CheckList /> : <div>dd</div>}
        </Grid>
      </Grid>
    </Container>
  );
}
