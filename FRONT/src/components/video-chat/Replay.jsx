import React, { useEffect, useRef, useState } from "react";
import videojs from "video.js";
import "video.js/dist/video-js.css";
import "@videojs/http-streaming";

const Replay = ({
  options,
  onReady,
  width = "100%",
  height = "auto",
  maxHeight = "500px",
}) => {
  const videoRef = useRef(null);
  const playerRef = useRef(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!playerRef.current) {
      const videoElement = videoRef.current;
      if (!videoElement) return;

      const player = (playerRef.current = videojs(videoElement, options, () => {
        videojs.log("player is ready");
        onReady && onReady(player);
      }));

      player.on("error", () => {
        console.error("Video Player Error:", player.error());
        setError(player.error().message);
      });
    } else {
      const player = playerRef.current;
      player.src(options.sources);
    }
  }, [options, onReady]);

  return (
    <div style={{ marginTop: "32px" }}>
      <div
        data-vjs-player
        style={{
          width,
          height,
          maxHeight,
          borderRadius: "8px",
          overflow: "hidden",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          position: "relative",
        }}
      >
        <video
          ref={videoRef}
          className="video-js vjs-big-play-centered"
          style={{
            width: "100%",
            height: "100%",
            objectFit: "contain",
            maxHeight: "100%",
          }}
        />
      </div>
      {error && <div>Error: {error}</div>}
    </div>
  );
};

export default Replay;
