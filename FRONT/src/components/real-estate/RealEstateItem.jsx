// 리스트에 뿌려지는 매물 아이템 카드
import React from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardMedia from "@mui/material/CardMedia";
import Typography from "@mui/material/Typography";

const RealEstateItem = ({ marker }) => {
  return (
    <Card sx={{ display: "flex", marginBottom: "20px" }}>
      <CardMedia
        component="img"
        sx={{ width: 151 }}
        image="https://via.placeholder.com/150" // 실제 이미지 URL로 변경
        alt={marker.title}
      />
      <CardContent>
        <Typography component="div">{marker.title}</Typography>
        <Typography variant="subtitle1" color="text.secondary" component="div">
          {marker.location}
        </Typography>
      </CardContent>
    </Card>
  );
};
export default RealEstateItem;
