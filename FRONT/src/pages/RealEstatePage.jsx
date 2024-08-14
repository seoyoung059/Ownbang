import React, { useEffect } from "react";
import RealEstateMap from "../components/real-estate/RealEstateMap";
import RealEstateSearchBar from "../components/real-estate/RealEstateSearchBar";
import RealEstateList from "../components/real-estate/RealEstateList";
import RealEstateDetail from "../components/real-estate/RealEstateDetail";
import Reservation from "../components/real-estate/Reservation";
import { Box, IconButton, CircularProgress } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { useTheme } from "@mui/material";
import { useBoundStore } from "../store/store";

const RealEstatePage = () => {
  const theme = useTheme();
  const {
    searchTerm,
    makeReservation,
    room,
    getRoom,
    realEstateData,
    getAllRoom,
    bookmarkList,
    getBookmarks,
    toggleBookmarks,
    getAvailableTime,
    getAgentAvailable,
    isAuthenticated,
    user,
  } = useBoundStore((state) => ({
    searchTerm: state.searchTerm,
    makeReservation: state.makeReservation,
    room: state.room,
    getRoom: state.getRoom,
    getAllRoom: state.getAllRoom,
    realEstateData: state.realEstateData,
    bookmarkList: state.bookmarkList,
    getBookmarks: state.getBookmarks,
    toggleBookmarks: state.toggleBookmarks,
    getAvailableTime: state.getAvailableTime,
    getAgentAvailable: state.getAgentAvailable,
    isAuthenticated: state.isAuthenticated,
    user: state.user,
  }));

  const [selectedItem, setSelectedItem] = React.useState(null);
  const [showReservation, setShowReservation] = React.useState(false);
  const [visibleMarkers, setVisibleMarkers] = React.useState([]);

  const [loading, setLoading] = React.useState(false);

  useEffect(() => {
    if (selectedItem) {
      setLoading(true);
      getRoom(selectedItem.id).finally(() => setLoading(false));
    }
  }, [selectedItem, getRoom]);

  // 매물 리스트에서 선택하는 항목
  const onSelectItem = async (item) => {
    setSelectedItem(item);
    setShowReservation(false); // 새로운 아이템 선택 시 예약 창 닫기
  };

  // 디테일 카드 닫기
  const onCloseDetailCard = () => {
    setSelectedItem(null);
    setShowReservation(false); // 디테일 카드 닫을 때 예약 창도 닫기
  };

  // 예약하기 버튼 클릭시 Reservation 카드 오픈
  const onOpenReservationCard = () => {
    setShowReservation(true);
  };

  // 예약 카드 닫기
  const onCloseReservationCard = () => {
    setShowReservation(false);
  };

  // 지도 경계 변경 시 호출되는 함수
  const onBoundsChange = (markers) => {
    setVisibleMarkers(markers);
  };

  const onIdle = (marker) => {
    setVisibleMarkers(marker);
  };

  // 마커 클릭 시 디테일 표시
  const onSelectMarker = (pos) => {
    const item = visibleMarkers.find((marker) => marker.id === pos.id);
    if (item) {
      setSelectedItem(item);
    }
  };

  return (
    <Box
      sx={{
        display: "flex",
        height: "98vh",
        overflow: "hidden",
        position: "relative",
      }}
    >
      {/* 리스트 */}
      <Box
        sx={{
          width: "22%",
          height: "100%",
          overflow: "auto", // RealEstateList에만 스크롤 적용
          display: "flex",
          flexDirection: "column",
        }}
      >
        <RealEstateList
          markers={visibleMarkers}
          onSelectItem={onSelectItem}
          selectedItem={selectedItem}
          bookmarkList={bookmarkList}
          getBookmarks={getBookmarks}
          toggleBookmarks={toggleBookmarks}
          isAuthenticated={isAuthenticated}
          user={user}
        />
      </Box>

      {/* 지도와 검색 바 */}
      <Box sx={{ width: "80%", height: "100%" }}>
        {/* 검색 바 오른쪽에 위치 */}
        <Box
          sx={{
            position: "fixed",
            zIndex: "999",
            padding: "20px",
            right: 48,
            top: 100,
          }}
        >
          <RealEstateSearchBar />
        </Box>
        <Box
          sx={{
            height: "100%",
            overflow: "hidden", // 지도 부분의 스크롤을 숨김
          }}
        >
          <RealEstateMap
            searchTerm={searchTerm}
            onIdle={onIdle}
            onSelectMarker={onSelectMarker}
          />
        </Box>
      </Box>

      {/* List에서 Item을 누르면 Detail 카드가 뜹니다 */}
      {selectedItem && (
        <Box
          sx={{
            position: "fixed",
            top: "12%",
            right: "50%",
            backgroundColor: theme.palette.background.default,
            padding: 3,
            borderRadius: 1,
            boxShadow: 3,
            width: "350px",
            height: "80%",
            overflow: "auto",
            zIndex: 50,
          }}
        >
          <IconButton
            onClick={onCloseDetailCard}
            sx={{
              position: "absolute",
              top: 8,
              right: 8, // X 아이콘을 카드의 우측 상단으로 이동
            }}
          >
            <CloseIcon />
          </IconButton>
          {loading ? (
            <Box
              sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: "100%",
              }}
            >
              <CircularProgress />
            </Box>
          ) : (
            <RealEstateDetail
              item={room}
              onOpenReservationCard={onOpenReservationCard}
              isAuthenticated={isAuthenticated}
              user={user}
            />
          )}
        </Box>
      )}

      {showReservation && (
        <Box
          sx={{
            position: "fixed",
            top: "12%",
            right: "22%",
            backgroundColor: theme.palette.background.default,
            padding: 3,
            borderRadius: 1,
            boxShadow: 3,
            width: "350px",
            height: "80%",
            overflow: "auto",
            zIndex: 999,
          }}
        >
          <IconButton
            onClick={onCloseReservationCard} // 예약 카드 닫기 핸들러
            sx={{
              position: "absolute",
              top: 8,
              right: 8, // X 아이콘을 카드의 우측 상단으로 이동
            }}
          >
            <CloseIcon />
          </IconButton>
          <Reservation
            makeReservation={makeReservation}
            item={selectedItem}
            getAvailableTime={getAvailableTime}
            getAgentAvailable={getAgentAvailable}
          />
        </Box>
      )}
    </Box>
  );
};

export default RealEstatePage;
