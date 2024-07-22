import React, { useDebugValue, useEffect, useRef, useState } from "react";
import { Map, MapMarker, MarkerClusterer } from "react-kakao-maps-sdk";
import clusterPositionsData from "./clusterPositionsData.json";

const loadKakaoMap = () => {
  const [positions, setPositions] = useState([]);
  useEffect(() => {
    setPositions(clusterPositionsData.tmpMarkers);
  }, []);

  return (
    <Map // 지도를 표시할 Container
      center={{
        // 지도의 중심좌표
        lat: 37.559551497191464,
        lng: 126.97086757997074,
      }}
      style={{
        // 지도의 크기
        width: "90%",
        height: "800px",
      }}
      level={9} // 지도의 확대 레벨
    >
      <MarkerClusterer
        averageCenter={true} // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
        minLevel={4} // 클러스터 할 최소 지도 레벨
      >
        {positions.map((pos) => (
          <MapMarker
            key={`${pos.lat}-${pos.lng}`}
            position={{
              lat: pos.lat,
              lng: pos.lng,
            }}
          />
        ))}
      </MarkerClusterer>
    </Map>
  );
};

export default loadKakaoMap;
