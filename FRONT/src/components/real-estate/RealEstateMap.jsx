import React, { useState, useEffect } from "react";
import { Map, MapMarker, MarkerClusterer } from "react-kakao-maps-sdk";
import clusterPositionsData from "./clusterPositionsData.json";

const RealEstateMap = ({
  searchTerm,
  mark,
  onBoundsChange,
  onSelectMarker,
}) => {
  const [positions, setPositions] = useState([]);
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

  const onMarkerClick = (pos) => {
    onSelectMarker(pos); // 전달
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
              key={pos.id}
              position={{
                lat: pos.lat,
                lng: pos.lng,
              }}
              onClick={() => onMarkerClick(pos)}
            />
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
