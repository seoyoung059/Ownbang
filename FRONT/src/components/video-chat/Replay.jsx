import React, { useEffect, useRef, useState } from "react";
import videojs from "video.js";
import "video.js/dist/video-js.css";
import "@videojs/http-streaming";

const Replay = ({
  options,
  onReady,
  width = "100%",
  height = "100%", // 컨테이너의 높이를 부모와 동일하게 설정
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
    <div style={{ height: height }}>
      {" "}
      {/* 부모 높이를 100%로 설정 */}
      <div
        data-vjs-player
        style={{
          width: width,
          height: "100%", // 부모의 높이를 100%로 유지
          borderRadius: "8px",
          overflow: "hidden",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          backgroundColor: "black", // 검은색 배경 추가
        }}
      >
        <video
          ref={videoRef}
          className="video-js vjs-big-play-centered"
          style={{
            width: "100%",
            height: "100%",
            objectFit: "contain", // 비율을 유지하며 컨테이너에 맞게 조정
          }}
        />
      </div>
      {error && <div>Error: {error}</div>}
    </div>
  );
};

export default Replay;
