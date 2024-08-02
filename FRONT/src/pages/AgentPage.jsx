import React from "react";
import MyRealEstate from "../components/user-info/MyRealEstate";
import { Box, Typography } from "@mui/material";
import { useTheme } from "@mui/material";

const myRealEstate = [
  {
    id: 0,
    latitude: 37.5,
    longitude: 127.039,
    dealType: "월세",
    roomType: "오피스텔",
    structure: "원룸",
    isLoft: false,
    exclusiveArea: 12.66,
    supplyArea: 18.33,
    roomFloor: 3,
    deposit: 20000000,
    monthlyRent: 670000,
    maintenanceFee: 99999,
    parcel: "변경된지번주소",
    profileImageUrl: "changedURL",
  },
  {
    id: 1,
    latitude: 37.561022215939886,
    longitude: 126.9811778682524,
    dealType: "매매",
    roomType: "상가",
    structure: "단일 구조",
    isLoft: false,
    exclusiveArea: 100.0,
    supplyArea: 150.0,
    roomFloor: 1,
    deposit: 500000000,
    monthlyRent: 500000,
    maintenanceFee: 1000000,
    parcel: "서울시 중구",
    profileImageUrl: "신세계백화점 본점",
  },
];

export default function AgentPage() {
  const theme = useTheme();
  return (
    <>
      <Box
        sx={{
          marginTop: 8,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Box sx={{ display: "flex", justifyContent: "center" }}>
          <Box>
            <Typography
              component="h1"
              variant="h5"
              sx={{ fontWeight: theme.fontWeight.bold, mt: 8 }}
            >
              내가 등록한 매물 리스트
            </Typography>
          </Box>
        </Box>
        <MyRealEstate myRealEstate={myRealEstate} />
      </Box>
    </>
  );
}
