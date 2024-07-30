import React from "react";
import { Box } from "@mui/material";

const UserVideoComponent = ({ streamManager }) => {
  if (!streamManager) return null;

  return (
    <Box
      sx={{
        width: "100%",
        height: "100%",
        borderRadius: "8px",
        overflow: "hidden",
      }}
    >
      <video
        autoPlay={true}
        id={"video-" + streamManager.stream.streamId}
        style={{ width: "100%", height: "100%", objectFit: "cover" }}
        ref={(vid) => {
          if (vid) streamManager.addVideoElement(vid);
        }}
      ></video>
    </Box>
  );
};

export default UserVideoComponent;
