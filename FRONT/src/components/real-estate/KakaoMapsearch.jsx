import React, { useEffect, useRef } from 'react';

const KakaoMapSearch = ({ searchTerm, mark, size }) => {
  const mapRef = useRef(null);

  useEffect(() => {
    if (!window.kakao || !searchTerm) return;

    const mapContainer = mapRef.current; // 지도를 표시할 div
    const mapOption = {
      center: new window.kakao.maps.LatLng(37.53269592749301, 126.99050764030287), // 초기 지도 중심 좌표
      level: 7, // 지도의 확대 레벨
    };

    const map = new window.kakao.maps.Map(mapContainer, mapOption);

    const searchInput = new window.kakao.maps.services.Places();

    searchInput.keywordSearch(searchTerm, (data, status, _pagination) => {
      if (status === window.kakao.maps.services.Status.OK) {
        const bounds = new window.kakao.maps.LatLngBounds();

        for (let i = 0; i < data.length; i++) {
          bounds.extend(new window.kakao.maps.LatLng(data[i].y, data[i].x));
        }

        map.setBounds(bounds);
        map.panTo(new window.kakao.maps.LatLng(data[0].y, data[0].x));
      } else {
        console.error('검색 결과가 없습니다.');
      }
    });
  }, [searchTerm]);

  useEffect(() => {
    if (!window.kakao || !mark) return;

    const mapContainer = mapRef.current;
    const mapOption = {
      center: new window.kakao.maps.LatLng(mark.lat, mark.lng),
      level: 7,
    };

    const map = new window.kakao.maps.Map(mapContainer, mapOption);
    const markerPosition = new window.kakao.maps.LatLng(mark.lat, mark.lng);
    const marker = new window.kakao.maps.Marker({
        position: markerPosition,
      });

    marker.setMap(map);
    map.panTo(markerPosition);
  }, [mark]);

  return (
    <div
      ref={mapRef}
      style={{
        width: size ? size : '100%',
        height: size ? size : '900px',
      }}
    ></div>
  );
};

export default KakaoMapSearch;
