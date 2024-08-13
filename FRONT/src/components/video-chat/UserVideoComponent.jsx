import React from "react";
import { Box } from "@mui/material";

const UserVideoComponent = ({ streamManager }) => {
  if (!streamManager) return null;

  return (
    <Box
      sx={{
        width: "100%",
        height: "100%",
        position: "relative",
        borderRadius: "8px",
        overflow: "hidden",
        background: "black",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <video
        autoPlay={true}
        id={"video-" + streamManager.stream.streamId}
        style={{
          width: "100%",
          height: "100%",
          objectFit: "contain",
          transform: "scaleX(-1)",
        }}
        ref={(vid) => {
          if (vid) streamManager.addVideoElement(vid);
        }}
      ></video>
    </Box>
  );
};

export default UserVideoComponent;
