import React, { useEffect, useRef, useState } from "react";
import videojs from "video.js";
import "video.js/dist/video-js.css";
import "@videojs/http-streaming";

const Replay = ({ options, onReady, width = "50%", height = "50%" }) => {
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

      player.on("error", (e) => {
        console.error("Video Player Error:", player.error());
        setError(player.error().message);
      });
    } else {
      // You can update player here
      const player = playerRef.current;
      player.src(options.sources);
    }
  }, [options, onReady]);

  const seekTo = (time) => {
    if (playerRef.current) {
      playerRef.current.currentTime(time);
    }
  };

  return (
    <div style={{ marginTop: "32px" }}>
      <div
        data-vjs-player
        style={{ width, height, borderRadius: "8px", overflow: "hidden" }}
      >
        <video ref={videoRef} className="video-js vjs-big-play-centered" />
      </div>
      {error && <div>Error: {error}</div>}
    </div>
  );
};

export default Replay;
