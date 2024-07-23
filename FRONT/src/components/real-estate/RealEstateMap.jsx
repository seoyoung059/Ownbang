import React, { useState, useEffect } from "react";
import { Map, MapMarker, MarkerClusterer } from "react-kakao-maps-sdk";
import clusterPositionsData from "./clusterPositionsData.json";
import { Box } from "@mui/material";

// Page에서 props로 searchTerm을 받아
const LoadKakaoMap = ({ searchTerm }) => {
  const [positions, setPositions] = useState([]);
  const [info, setInfo] = useState(null);
  const [map, setMap] = useState(null);

  useEffect(() => {
    setPositions(clusterPositionsData.tmpMarkers);
  }, []);

  useEffect(() => {
    if (!map || !searchTerm) return;

    const searchInput = new kakao.maps.services.Places();

    searchInput.keywordSearch(searchTerm, (data, status, _pagination) => {
      if (status === kakao.maps.services.Status.OK) {
        const bounds = new kakao.maps.LatLngBounds();

        for (let i = 0; i < data.length; i++) {
          bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
        }

        map.setBounds(bounds); // 검색 결과에 따라 지도 범위 재설정
      }
    });
  }, [map, searchTerm]);

  const handleMarkerClick = (pos) => {
    setInfo(info && info.lat === pos.lat && info.lng === pos.lng ? null : pos);
  };

  return (
    <Map // 지도 표시 container
      center={{
        // 지도 중심좌표 (현재 용산구청 부근, 나중에 나의 좌표로 변경 예정)
        lat: 37.53269592749301,
        lng: 126.99050764030287,
      }}
      style={{
        // 지도 크기
        width: "100%",
        height: "900px",
      }}
      level={7}
      onCreate={setMap} // 지도가 생성될 때 setMap 호출
    >
      <MarkerClusterer averageCenter={true} minLevel={3}>
        {positions.map((pos) => (
          <MapMarker
            key={`${pos.lat}-${pos.lng}`}
            position={{
              lat: pos.lat,
              lng: pos.lng,
              touchAction: "pan-x pan-y",
            }}
            onClick={() => handleMarkerClick(pos)}
          >
            {info && info.lat === pos.lat && info.lng === pos.lng && (
              <Box
                sx={{
                  color: "darkGray",
                }}
              >
                {pos.title}
              </Box>
            )}
          </MapMarker>
        ))}
      </MarkerClusterer>
    </Map>
  );
};

export default LoadKakaoMap;
