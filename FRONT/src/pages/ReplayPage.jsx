import React, { useEffect, useState, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useBoundStore } from "../store/store";
import Replay from "../components/video-chat/Replay";
import CheckList from "../components/checklist/CheckList";
import VideoLoading from "../components/video-chat/VideoLoading";
import { Container, Grid, Box, Typography, Button } from "@mui/material";
import { useTheme } from "@emotion/react";

const ReplayPage = () => {
  const theme = useTheme();
  const location = useLocation();
  const navigate = useNavigate();
  const [isDataLoaded, setIsDataLoaded] = useState(false);
  const [videoInfo, setVideoInfo] = useState(null);
  const [player, setPlayer] = useState(null);
  const [currentTime, setCurrentTime] = useState("");

  const { enterReplayRoom } = useBoundStore((state) => ({
    enterReplayRoom: state.enterReplayRoom,
  }));

  useEffect(() => {
    console.log(location.state);
    const fetchVideoInfo = async () => {
      try {
        const result = await enterReplayRoom(location.state.reservationId);
        setVideoInfo(result);
        setIsDataLoaded(true);
      } catch (error) {
        console.error("Error fetching video info:", error);
      }
    };

    fetchVideoInfo();
  }, [enterReplayRoom]);

  const videoJsOptions = {
    autoplay: true,
    controls: true,
    responsive: true,
    fluid: true,
    sources: [
      {
        src: videoInfo ? videoInfo.videoUrl : "",
        type: "application/x-mpegURL",
      },
    ],
    html5: {
      hls: {
        enableLowInitialPlaylist: true,
        smoothQualityChange: true,
        overrideNative: true,
      },
    },
  };

  const handlePlayerReady = (player) => {
    console.log("Player is ready");
    setPlayer(player);
    player.on("waiting", () => {
      console.log("player is waiting");
    });

    player.on("dispose", () => {
      console.log("player will dispose");
    });
  };

  const handleTimestampClick = (timestamp) => {
    if (player) {
      const seconds =
        parseInt(timestamp.split(":")[0]) * 60 +
        parseInt(timestamp.split(":")[1]);

      const videoDuration = player.duration();

      if (seconds <= videoDuration) {
        player.currentTime(seconds);
        console.log(`Navigating to ${timestamp}`);
      } else {
        window.alert("잘못된 탬플릿을 선택하셨습니다.");
        console.log(
          `Timestamp ${timestamp} exceeds video duration. Navigation cancelled.`
        );
      }
    }
  };

  const handleExit = () => {
    navigate("/mypage"); // Navigate to "mypage"
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 16 }}>
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <Typography
          variant="h5"
          gutterBottom
          sx={{ fontWeight: theme.fontWeight.bold }}
        >
          다시보기
        </Typography>
        <Button variant="contained" color="secondary" onClick={handleExit}>
          나가기
        </Button>
      </Box>
      <Grid container spacing={3}>
        <Grid item xs={12} md={8}>
          {isDataLoaded ? (
            <Replay
              options={videoJsOptions}
              onReady={handlePlayerReady}
              width="100%"
              height="100%"
              maxHeight="200px"
            />
          ) : (
            <VideoLoading />
          )}
        </Grid>
        <Grid item xs={12} md={4} sx={{ mt: 4 }}>
          <Box>
            <CheckList
              canEdit={true}
              forReplay={true}
              onTimestampClick={handleTimestampClick}
            />
          </Box>
        </Grid>
      </Grid>
    </Container>
  );
};

export default ReplayPage;
