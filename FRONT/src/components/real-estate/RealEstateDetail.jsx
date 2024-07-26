// 리스트에 뿌려진 매물 아이템 클릭 시 보이는 매물 상세 컴퍼넌트
import React from "react";
import { Button, Typography, Box } from "@mui/material";

const RealEstateDetail = ({ item }) => {
  return (
    <Box sx={{ padding: 2 }}>
      <Typography variant="h4" sx={{ marginTop: 2 }}>
        {item.title}
      </Typography>
      <Typography variant="subtitle1" color="text.secondary">
        Location: {item.location}
      </Typography>
      <Typography variant="body1" sx={{ marginTop: 2 }}>
        {item.description}
      </Typography>
    </Box>
  );
};

export default RealEstateDetail;
