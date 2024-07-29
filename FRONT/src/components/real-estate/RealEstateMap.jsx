import React, { useState, useEffect } from "react";
import { Map, MapMarker, MarkerClusterer } from "react-kakao-maps-sdk";
import { Box } from "@mui/material";
import clusterPositionsData from "./clusterPositionsData.json";

const LoadKakaoMap = ({ searchTerm, mark, size }) => {
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
        map.panTo(new kakao.maps.LatLng(data[0].y, data[0].x)); // 첫 번째 결과를 중심으로 이동
      } else {
        console.error("검색 결과가 없습니다.");
      }
    });
  }, [map, searchTerm]);

  useEffect(() => {
    if (!map || !mark) return;

    const center = new kakao.maps.LatLng(mark.lat, mark.lng);
    map.panTo(center); // mark 좌표를 중심으로 지도 이동
  }, [map, mark]);

  const handleMarkerClick = (pos) => {
    setInfo(info && info.lat === pos.lat && info.lng === pos.lng ? null : pos);
  };

  return (
    <Map // 지도 표시 container
      center={{
        lat: 37.53269592749301,
        lng: 126.99050764030287,
      }}
      style={{
        width: "100%",
        height: size,
      }}
      level={7}
      onCreate={setMap} // 지도가 생성될 때 setMap 호출
    >
      {!mark ? (
        <MarkerClusterer averageCenter={true} minLevel={3}>
          {positions.map((pos) => (
            <MapMarker
              key={`${pos.lat}-${pos.lng}`}
              position={{
                lat: pos.lat,
                lng: pos.lng,
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
      ) : (
        <MapMarker
          position={{
            lat: mark.lat,
            lng: mark.lng,
          }}
        />
      )}
    </Map>
  );
};

export default LoadKakaoMap;
