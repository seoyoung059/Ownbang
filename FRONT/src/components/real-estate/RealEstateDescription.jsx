import React from "react";
import { Typography, Box } from "@mui/material";
import CropIcon from "@mui/icons-material/Crop";
import MeetingRoomIcon from "@mui/icons-material/MeetingRoom";
import DomainIcon from "@mui/icons-material/Domain";
import DirectionsCarIcon from "@mui/icons-material/DirectionsCar";
import ElevatorIcon from "@mui/icons-material/Elevator";
import { useTheme } from "@mui/material";

const InfoItem = ({ icon: Icon, text }) => (
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
  // const detailInfo = item.roomDetailSearchResponse;
  // const appliancesInfo = item.roomAppliancesSearchResponse;

  console.log(item);
  if (!detailInfo) {
    return null;
  }

  if (!appliancesInfo) {
    return null;
  }

  console.log("Room Detail Info:", detailInfo);
  console.log("Appliances Info:", appliancesInfo);

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
        item.roomFloor && detailInfo && detailInfo.buildingFloor
          ? `${item.roomFloor}층/${detailInfo.buildingFloor}층`
          : "정보 없음",
    },
    {
      icon: DirectionsCarIcon,
      text: detailInfo.parking
        ? `${detailInfo.totalParking}대 주차 가능`
        : "주차 불가",
    },
    {
      icon: ElevatorIcon,
      text: detailInfo.elevator ? "엘레베이터 있음" : "엘레베이터 없음",
    },
  ];

  const applianceText = {
    bed: "침대",
    washingMachine: "세탁기",
    airConditioner: "에어컨",
    closet: "옷장",
    desk: "책상",
    microwave: "전자레인지",
    refrigerator: "냉장고",
    chair: "의자",
  };

  const appliances = Object.keys(appliancesInfo)
    .filter((key) => appliancesInfo[key])
    .map((key) => applianceText[key])
    .join(" ");

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
      {appliances && (
        <Box sx={{ padding: 2, marginTop: 2 }}>
          <Typography sx={{}}>구비 완료 | {appliances}</Typography>
        </Box>
      )}
    </Box>
  );
};

export default RealEstateDescription;
