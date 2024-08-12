import React, { useState } from "react";
import {
  Box,
  Button,
  Modal,
  Typography,
  Rating,
  IconButton,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";

export default function ReviewModal({
  open,
  onClose,
  onSubmit,
  reservationId,
  agentId,
  AgentOfficeName,
}) {
  const [starRating, setStarRating] = useState(0);
  const [content, setContent] = useState("");

  const handleSubmit = () => {
    const reviewData = {
      reservationId,
      agentId,
      starRating,
      content,
    };
    onSubmit(reviewData);
    onClose();
  };

  return (
    <Modal
      open={open}
      onClose={onClose}
      aria-labelledby="review-modal-title"
      aria-describedby="review-modal-description"
    >
      <Box
        sx={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          width: 400,
          bgcolor: "background.paper",
          borderRadius: 2,
          boxShadow: 24,
          p: 4,
        }}
      >
        <Box sx={{ display: "flex", justifyContent: "space-between", mb: 2 }}>
          <Typography id="review-modal-title" variant="h6" component="h2">
            {AgentOfficeName}님은 어땠나요?
          </Typography>
          <IconButton onClick={onClose}>
            <CloseIcon />
          </IconButton>
        </Box>
        <Rating
          name="star-rating"
          value={starRating}
          onChange={(event, newValue) => {
            setStarRating(newValue);
          }}
        />
        {/* <TextField
          fullWidth
          id="review-content"
          label="리뷰 내용"
          multiline
          rows={4}
          variant="outlined"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          sx={{ mt: 2 }}
        /> */}
        <Box
          sx={{
            display: "flex",
            flexDirection: "column", // 세로 정렬로 변경
            gap: 2, // 버튼 사이 간격 조정
            mt: 2,
          }}
        >
          <Button variant="contained" onClick={handleSubmit}>
            제출하기
          </Button>
        </Box>
      </Box>
    </Modal>
  );
}
