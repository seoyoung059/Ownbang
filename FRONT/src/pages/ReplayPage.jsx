// ReplayPage.jsx
import React, { useEffect, useState, useRef } from "react";
import { useBoundStore } from "../store/store";
import Replay from "../components/video-chat/Replay";
import CheckList from "../components/checklist/CheckList";

const ReplayPage = () => {
  const [isDataLoaded, setIsDataLoaded] = useState(false);
  const [videoInfo, setVideoInfo] = useState(null);
  const [player, setPlayer] = useState(null); // 플레이어 상태 추가

  const { enterReplayRoom } = useBoundStore((state) => ({
    enterReplayRoom: state.enterReplayRoom,
  }));

  useEffect(() => {
    const fetchVideoInfo = async () => {
      try {
        const result = await enterReplayRoom(17); // 17은 예제 ID입니다. 실제로는 동적으로 받을 수 있습니다.
        setVideoInfo(result); // 데이터가 로드되면 상태를 업데이트합니다.
        setIsDataLoaded(true); // 데이터가 로드되었음을 상태로 설정합니다.
      } catch (error) {
        console.error("Error fetching video info:", error); // 에러 처리
      }
    };

    fetchVideoInfo(); // 비동기 데이터 로드 호출
  }, [enterReplayRoom]);

  const videoJsOptions = {
    autoplay: true,
    controls: true,
    responsive: true,
    fluid: true,
    sources: [
      {
        src: videoInfo ? videoInfo.videoUrl : "", // URL이 없으면 빈 문자열
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
    setPlayer(player); // 플레이어 상태 업데이트
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
      player.currentTime(seconds);
      console.log(seconds);
    }
  };

  return (
    <div>
      <h1>다시보기</h1>
      <div style={{ display: "flex" }}>
        {isDataLoaded ? (
          <Replay
            options={videoJsOptions}
            onReady={handlePlayerReady}
            width="640px"
            height="360px"
          />
        ) : (
          <p>비디오 정보를 로드 중입니다...</p> // 데이터가 로드되기 전 표시할 내용
        )}
        <CheckList canEdit={true} onTimestampClick={handleTimestampClick} />
      </div>
    </div>
  );
};

export default ReplayPage;
