import React from "react";
import { Typography, Box } from "@mui/material";
import CropIcon from "@mui/icons-material/Crop";
import MeetingRoomIcon from "@mui/icons-material/MeetingRoom";
import DomainIcon from "@mui/icons-material/Domain";
import DirectionsCarIcon from "@mui/icons-material/DirectionsCar";
import LaptopChromebookIcon from "@mui/icons-material/LaptopChromebook";
import { useTheme } from "@mui/material";

const InfoItem = ({ icon: Icon, text, item }) => (
  <Box
    sx={{
      display: "flex",
      flexDirection: "row",
      alignItems: "center",
      gap: 2,
    }}
  >
    <Icon />
    <Typography>{text}</Typography>
  </Box>
);

const RealEstateDescription = ({ item }) => {
  const theme = useTheme();
  const detailInfo = item.roomDetailSearchResponse;
  const applianceInfo = item.roomAppliancesSearchResponse;
  const infoItems = [
    {
      icon: CropIcon,
      text: item.exclusiveArea ? `${item.exclusiveArea}㎡` : "정보 없음",
    },
    {
      icon: MeetingRoomIcon,
      text:
        item.structure && item.roomType
          ? `${item.structure} ${item.roomType}`
          : "정보 없음",
    },
    {
      icon: DomainIcon,
      text:
        item.roomFloor && detailInfo.buildingFloor
          ? `${item.roomFloor}층/${detailInfo.buildingFloor}층`
          : "정보 없음",
      // text: item.roomFloor ? `${item.roomFloor}층` : "정보 없음",
    },
    { icon: DirectionsCarIcon, text: item.parking ? "주차 가능" : "주차 불가" },
    { icon: LaptopChromebookIcon, text: "화상통화 즉시 예약 가능" },
  ];

  return (
    <Box>
      <Typography variant="h6" sx={{ marginBottom: 1 }}>
        이 집을 소개합니다.
      </Typography>
      <Typography
        variant="body2"
        sx={{
          fontWeight: 300,
          color: theme.palette.text.secondary,
          opacity: 0.7,
          marginBottom: 2,
        }}
      >
        화상을 통해 이 집의 상태를 구체적으로 확인할 수 있습니다.
      </Typography>

      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          gap: 3,
          backgroundColor: theme.palette.background.paper,
          padding: 3,
          borderRadius: 1,
          boxShadow: 1,
        }}
      >
        {infoItems.map((item, index) => (
          <InfoItem key={index} icon={item.icon} text={item.text} />
        ))}
      </Box>
    </Box>
  );
};

export default RealEstateDescription;
