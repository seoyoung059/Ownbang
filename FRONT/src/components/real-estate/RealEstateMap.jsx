import React, { useState, useEffect, useCallback } from "react";
import { Map, MapMarker, MarkerClusterer } from "react-kakao-maps-sdk";
import { useBoundStore } from "../../store/store";

const RealEstateMap = ({
  searchTerm,
  mark,
  onBoundsChange,
  onIdle,
  onSelectMarker,
  size,
}) => {
  const { realEstateData, getAllRoom } = useBoundStore((state) => ({
    realEstateData : state.realEstateData,
    getAllRoom : state.getAllRoom
  }));
  const [map, setMap] = useState(null);

  useEffect(() => {
    handleGetRoom();
  }, [map]);

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
    onSelectMarker(pos);
  };

  const onBoundsChange2 = () => {
    if (!map) return;
    const bounds = map.getBounds();
    const visiblePositions =
      realEstateData&&
      realEstateData.filter((pos) =>
        bounds.contain(new kakao.maps.LatLng(pos.latitude, pos.longitude))
      );
    onIdle(visiblePositions)
  };

  const handleGetRoom = async () => {
    if (!map) return;
    try {
      const center = map.getCenter();
      console.log(center.getLat(), center.getLng())
      const rooms = await getAllRoom(center.getLat(), center.getLng());
    } catch (error) {
      console.log(error)
    }
  };

  const handleIdle = async() => {
      debouncedHandleGetRoom();
      onBoundsChange2()
  };

  useEffect(() => {
    if (map) {
      onBoundsChange2();
    }
  }, [map, realEstateData]);

  const debounce = (func, delay) => {
    let timeout;
    return (...args) => {
      clearTimeout(timeout);
      timeout = setTimeout(() => func(...args), delay);
    };
  };


  const debouncedHandleGetRoom = useCallback(debounce(async () => {
    if (!map) return;
    try {
      const center = map.getCenter();
      console.log(center.getLat(), center.getLng());

      await getAllRoom(center.getLat(), center.getLng());
    } catch (error) {
      console.log(error);
    }
  }, 250), [map]);

  return (
    <Map
      center={{
        lat: 37.53269592749301,
        lng: 126.99050764030287,
      }}
      style={{
        width: "100%",
        height: size ? size : "900px",
      }}
      level={7}
      maxLevel={3}
      minLevel={6}
      onCreate={setMap}
      onIdle={handleIdle}
    >
      {!mark ? (
        <MarkerClusterer averageCenter={true} minLevel={5}>
          {realEstateData&&
            realEstateData.map((pos) => (
              <MapMarker
                key={pos.id}
                position={{
                  lat: pos.latitude,
                  lng: pos.longitude,
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
