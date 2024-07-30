// RealEstateMap.jsx
import React, { useState, useEffect } from "react";
import { Map, MapMarker, MarkerClusterer } from "react-kakao-maps-sdk";
import { Box } from "@mui/material";
import { useBoundStore } from "../../store/store";
import clusterPositionsData from "./clusterPositionsData.json";

const RealEstateMap = ({ mark, size, onBoundsChange, onMarkerClick }) => {
  const [positions, setPositions] = useState([]);
  const [info, setInfo] = useState(null);
  const [map, setMap] = useState(null);
  const searchTerm = useBoundStore((state) => state.searchTerm);

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

        map.setBounds(bounds);
        map.panTo(new kakao.maps.LatLng(data[0].y, data[0].x));
      } else {
        console.error("검색 결과가 없습니다.");
      }
    });
  }, [map, searchTerm]);

  useEffect(() => {
    if (!map || !mark) return;

    const center = new kakao.maps.LatLng(mark.lat, mark.lng);
    map.panTo(center);
  }, [map, mark]);

  const onMarkerClickInternal = (pos) => {
    setInfo(info && info.lat === pos.lat && info.lng === pos.lng ? null : pos);
    onMarkerClick(pos);
  };

  const onBoundsChange2 = () => {
    if (!map) return;
    const bounds = map.getBounds();
    const visiblePositions = positions.filter((pos) =>
      bounds.contain(new kakao.maps.LatLng(pos.lat, pos.lng))
    );
    onBoundsChange(visiblePositions);
  };

  useEffect(() => {
    if (map) {
      onBoundsChange2();
    }
  }, [map, positions]);

  return (
    <Map
      center={{
        lat: 37.53269592749301,
        lng: 126.99050764030287,
      }}
      style={{
        width: "100%",
        height: "900px",
      }}
      level={7}
      onCreate={setMap}
      onBoundsChanged={onBoundsChange2}
    >
      {!mark ? (
        <MarkerClusterer averageCenter={true} minLevel={5}>
          {positions.map((pos) => (
            <MapMarker
              key={`${pos.lat}-${pos.lng}`}
              position={{
                lat: pos.lat,
                lng: pos.lng,
              }}
              onClick={() => onMarkerClickInternal(pos)}
            >
              {info && info.lat === pos.lat && info.lng === pos.lng && (
                <Box sx={{ color: "darkGray" }}>{pos.title}</Box>
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

export default RealEstateMap;
