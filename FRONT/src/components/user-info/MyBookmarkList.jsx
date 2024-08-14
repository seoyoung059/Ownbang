import React, { useState, useEffect } from "react";
import {
  Container,
  Typography,
  Grid,
  CircularProgress,
  Box,
  IconButton,
} from "@mui/material";
import MyBookmarkItem from "./MyBookmarkItem";
import RealEstateDetail from "../real-estate/RealEstateDetail";
import Reservation from "../real-estate/Reservation";
import CloseIcon from "@mui/icons-material/Close";
import { useTheme } from "@mui/material";
import { useBoundStore } from "../../store/store";
import axios from "axios"; // Axios 또는 원하는 HTTP 클라이언트 라이브러리 import

function MyBookmarkList() {
  const theme = useTheme();
  const {
    bookmarkList,
    getBookmarks,
    getRoom,
    isAuthenticated,
    user,
    getAgentAvailable,
    makeReservation,
  } = useBoundStore((state) => ({
    bookmarkList: state.bookmarkList,
    getBookmarks: state.getBookmarks,
    getRoom: state.getRoom,
    isAuthenticated: state.isAuthenticated,
    user: state.user,
    getAgentAvailable: state.getAgentAvailable,
    makeReservation: state.makeReservation,
  }));

  const [loading, setLoading] = useState(true);
  const [detailedBookmarks, setDetailedBookmarks] = useState([]);
  const [selectedItem, setSelectedItem] = useState(null);
  const [showDetailCard, setShowDetailCard] = useState(false);
  const [showReservationCard, setShowReservationCard] = useState(false);

  useEffect(() => {
    const fetchBookmarks = async () => {
      setLoading(true);
      try {
        await getBookmarks();
      } finally {
        setLoading(false);
      }
    };
    fetchBookmarks();
  }, [getBookmarks]);

  useEffect(() => {
    const fetchDetailsForBookmarks = async () => {
      if (bookmarkList.length === 0) return;

      try {
        const details = await Promise.all(
          bookmarkList.map(async (bookmark) => {
            const roomId = bookmark.roomInfoSearchResponse?.id;
            if (roomId) {
              await getRoom(roomId);
              const roomDetails = useBoundStore.getState().room;
              return { ...bookmark, roomDetails };
            } else {
              return { ...bookmark, roomDetails: {} };
            }
          })
        );
        setDetailedBookmarks(details);
      } catch (error) {
        console.error("Error fetching room details:", error);
      }
    };

    fetchDetailsForBookmarks();
  }, [bookmarkList, getRoom]);

  const handleSelectItem = (bookmark) => {
    setSelectedItem(bookmark);
    setShowDetailCard(true);
  };

  const handleCloseDetailCard = () => {
    setSelectedItem(null);
    setShowDetailCard(false);
    setShowReservationCard(false);
  };

  const handleOpenReservationCard = () => {
    setShowReservationCard(true);
  };

  if (loading) {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          mt: 10,
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Container
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        padding: 2,
      }}
    >
      {detailedBookmarks.length === 0 ? (
        <Typography variant="h6" color="text.secondary">
          찜 목록이 없습니다.
        </Typography>
      ) : (
        <Grid container spacing={2}>
          {detailedBookmarks.map((bookmark) => (
            <Grid item xs={12} sm={6} key={bookmark.id}>
              <MyBookmarkItem
                bookmark={bookmark}
                onSelect={() => handleSelectItem(bookmark)}
              />
            </Grid>
          ))}
        </Grid>
      )}

      {showDetailCard && selectedItem && (
        <Box
          sx={{
            position: "fixed",
            top: "12%",
            backgroundColor: theme.palette.background.default,
            padding: 3,
            borderRadius: 1,
            boxShadow: 3,
            width: "800px",
            height: "80%",
            overflow: "auto",
            zIndex: 50,
          }}
        >
          <IconButton
            onClick={handleCloseDetailCard}
            sx={{
              position: "absolute",
              top: 8,
              right: 8,
            }}
          >
            <CloseIcon />
          </IconButton>
          <RealEstateDetail
            item={selectedItem.roomDetails}
            onOpenReservationCard={handleOpenReservationCard}
            isAuthenticated={isAuthenticated}
            user={user}
          />
        </Box>
      )}

      {showReservationCard && selectedItem && (
        <Box
          sx={{
            position: "fixed",
            top: "12%",
            backgroundColor: theme.palette.background.default,
            padding: 3,
            borderRadius: 1,
            boxShadow: 3,
            width: "800px",
            height: "80%",
            overflow: "auto",
            zIndex: 100,
          }}
        >
          <IconButton
            onClick={handleCloseDetailCard}
            sx={{
              position: "absolute",
              top: 8,
              right: 8,
            }}
          >
            <CloseIcon />
          </IconButton>
          <Reservation
            item={selectedItem.roomDetails}
            onClose={() => setShowReservationCard(false)}
            getAgentAvailable={getAgentAvailable}
            makeReservation={makeReservation}
          />
        </Box>
      )}
    </Container>
  );
}

export default MyBookmarkList;
