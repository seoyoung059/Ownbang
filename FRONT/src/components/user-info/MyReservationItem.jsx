import React, { useState } from "react";
import {
  TableRow,
  TableCell,
  Avatar,
  Typography,
  Button,
  Box,
} from "@mui/material";
import {
  CheckCircleOutlined,
  CancelOutlined,
  PendingOutlined,
  HistoryOutlined,
  FiberManualRecord,
} from "@mui/icons-material";
import { useMediaQuery, useTheme } from "@mui/material";
import { useNavigate } from "react-router-dom";
import ReviewModal from "./ReviewModal";
import { useBoundStore } from "../../store/store";
import { toast } from "react-toastify";

export default function MyReservationItem({
  reservation,
  refreshReservations,
}) {
  const theme = useTheme();
  const navigate = useNavigate();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  const [isHovered, setIsHovered] = useState(false);
  const [isReviewModalOpen, setReviewModalOpen] = useState(false);

  const { writeReview, killReview } = useBoundStore((state) => ({
    writeReview: state.writeReview,
    killReview: state.killReview,
  }));

  const getStatusIconAndText = (status) => {
    switch (status) {
      case "CONFIRMED":
        return {
          icon: <CheckCircleOutlined style={{ color: "green" }} />,
          text: "확정",
        };
      case "APPLYED":
        return {
          icon: <PendingOutlined style={{ color: "orange" }} />,
          text: "대기",
        };
      case "CANCELLED":
        return {
          icon: <CancelOutlined style={{ color: "red" }} />,
          text: "취소",
        };
      case "COMPLETED":
        return {
          icon: (
            <HistoryOutlined style={{ color: theme.palette.primary.dark }} />
          ),
          text: "완료",
        };
      case "ENCODING":
        return {
          icon: (
            <FiberManualRecord style={{ color: theme.palette.error.main }} />
          ),
          text: "처리 중",
        };
      default:
        return { icon: null, text: "알 수 없음" };
    }
  };

  const { icon, text } = getStatusIconAndText(reservation.status);

  const handleReviewSubmit = async (reviewData) => {
    await writeReview(reviewData);
    await toast.success("리뷰 등록 완료", {
      position: "bottom-left",
      autoClose: 2000,
      hideProgressBar: true,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: "light",
    });
    await setTimeout(refreshReservations, 500);
  };

  const handleReviewDelete = async () => {
    await killReview(reservation.id);
    await toast.success("리뷰 삭제 완료", {
      position: "bottom-left",
      autoClose: 2000,
      hideProgressBar: true,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: "light",
    });
    await setTimeout(refreshReservations, 500);
  };

  const reviewText = reservation.isReview ? "리뷰 작성" : "리뷰 삭제";

  return (
    <TableRow sx={{ bgcolor: theme.palette.background.default }}>
      {!isMobile && (
        <TableCell sx={{ padding: "4px 8px" }}>
          <Avatar
            src={reservation.roomProfileImage}
            variant="rounded"
            sx={{ width: "150px", height: "90px" }}
          />
        </TableCell>
      )}
      <TableCell>
        <Typography variant={isMobile ? "body2" : "body1"}>
          {new Date(reservation.reservationTime).toLocaleDateString("ko-KR")}
        </Typography>
      </TableCell>
      <TableCell>
        <Typography variant={isMobile ? "body2" : "body1"}>
          {new Date(reservation.reservationTime).toLocaleTimeString("ko-KR", {
            hour: "2-digit",
            minute: "2-digit",
          })}
        </Typography>
      </TableCell>
      {!isMobile && (
        <TableCell>
          <Typography variant={"body1"}>
            {reservation.AgentOfficeName}
          </Typography>
        </TableCell>
      )}
      <TableCell sx={{ minWidth: 100, maxWidth: 100 }}>
        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            cursor: reservation.status === "COMPLETED" ? "pointer" : "default",
            color:
              isHovered && reservation.status === "COMPLETED"
                ? reservation.isReview
                  ? theme.palette.warning.main // Yellow color for "리뷰 수정"
                  : theme.palette.error.main // Red color for "리뷰 삭제"
                : "inherit",
            transition: "color 0.3s ease",
          }}
          onMouseEnter={() => setIsHovered(true)}
          onMouseLeave={() => setIsHovered(false)}
          onClick={
            reservation.status === "COMPLETED"
              ? reservation.isReview
                ? () => setReviewModalOpen(true) // Open modal for review update
                : handleReviewDelete // Directly delete review
              : undefined
          }
        >
          {reservation.status === "COMPLETED" && isHovered ? (
            <Typography
              sx={{ ml: 1, whiteSpace: "nowrap", textAlign: "center" }}
              variant={"body1"}
            >
              {reviewText}
            </Typography>
          ) : (
            <>
              {icon}
              <Typography
                sx={{ ml: 1, whiteSpace: "nowrap", textAlign: "center" }}
                variant={"body1"}
              >
                {text}
              </Typography>
            </>
          )}
        </Box>
      </TableCell>
      <TableCell>
        {reservation.status === "ENCODING" ? (
          <Typography variant="body1" color="textSecondary">
            처리 중
          </Typography>
        ) : reservation.status !== "COMPLETED" ? (
          <Button
            variant="contained"
            disabled={!reservation.enstance}
            size={isMobile ? "small" : "medium"}
            onClick={() =>
              navigate(`/video-chat/${reservation.id}`, {
                state: { reservationId: reservation.id },
              })
            }
          >
            입장하기
          </Button>
        ) : (
          <Button
            variant="contained"
            sx={{
              backgroundColor: theme.palette.primary.dark,
              "&:hover": {
                backgroundColor: theme.palette.primary.main,
              },
            }}
            size={isMobile ? "small" : "medium"}
            onClick={() =>
              navigate(`/replay/${reservation.id}`, {
                state: { reservationId: reservation.id },
              })
            }
          >
            다시보기
          </Button>
        )}
      </TableCell>

      {/* Review Modal */}
      {reservation.isReview && (
        <ReviewModal
          open={isReviewModalOpen}
          onClose={() => setReviewModalOpen(false)}
          onSubmit={handleReviewSubmit}
          AgentOfficeName={reservation.AgentOfficeName}
          reservationId={reservation.id}
          agentId={reservation.agentId}
        />
      )}
    </TableRow>
  );
}
