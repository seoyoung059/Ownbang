import React from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Box,
  Rating,
} from "@mui/material";

import { useBoundStore } from "../../store/store";

const ReviewForm = ({
  reservationId,
  agentId,
  open,
  review,
  rating,
  handleReviewChange,
  handleRatingChange,
  handleCloseReviewDialog,
  handleSubmitReview,
}) => {
  const { writeReview } = useBoundStore((state) => ({
    writeReview: state.writeReview,
  }));

  const onSubmitReview = async () => {
    await writeReview({
      reservationId: reservationId,
      agentId: agentId,
      starRating: rating,
      content: review,
    });
    handleSubmitReview(); // 리뷰 제출 후 다이얼로그 닫기
  };

  return (
    <Dialog open={open} onClose={handleCloseReviewDialog}>
      <DialogTitle>리뷰 작성</DialogTitle>
      <DialogContent>
        <TextField
          autoFocus
          margin="dense"
          label="리뷰"
          type="text"
          fullWidth
          value={review}
          onChange={handleReviewChange}
        />
        <Box sx={{ mt: 2 }}>
          <Rating
            name="simple-controlled"
            value={rating}
            onChange={handleRatingChange}
          />
        </Box>
      </DialogContent>
      <DialogActions>
        <Button onClick={handleCloseReviewDialog} color="primary">
          취소
        </Button>
        <Button onClick={onSubmitReview} color="primary">
          제출
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ReviewForm;
