import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Box, Button, Divider, Container } from "@mui/material";
import { useTheme } from "@mui/material";
import AgentRealEstate from "../components/user-info/AgentRealEstate";
import AgentReservationList from "../components/user-info/AgentReservationList";

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

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`tabpanel-${index}`}
      aria-labelledby={`tab-${index}`}
      {...other}
    >
      {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
    </div>
  );
}

export default function AgentPage() {
  const [tabValue, setTabValue] = useState(0);
  const navigate = useNavigate();
  const theme = useTheme();

  const handleChange = (newValue) => {
    setTabValue(newValue);
  };

  const toUserEdit = () => {
    navigate("/user-edit");
  };
  return (
    <>
      <Box sx={{ mt: 16 }}>
        <Box sx={{ textAlign: "end" }}>
          <Button
            variant="text"
            onClick={toUserEdit}
            size="small"
            sx={{
              color: theme.palette.common.black,
              border: `0.5px solid ${theme.palette.common.black}`,
            }}
          >
            회원 정보 변경
          </Button>
        </Box>
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            mt: 2,
          }}
        >
          <Button
            variant={tabValue === 0 ? "contained" : "outlined"}
            size="large"
            onClick={() => handleChange(0)}
            sx={{ mx: 1 }}
          >
            예약목록
          </Button>
          <Divider orientation="vertical" flexItem />
          <Button
            variant={tabValue === 1 ? "contained" : "outlined"}
            size="large"
            onClick={() => handleChange(1)}
            sx={{ mx: 1 }}
          >
            등록한 매물 리스트
          </Button>
        </Box>

        <Box
          sx={{
            mt: 4,
            width: "100%",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            flexGrow: 1,
          }}
        >
          <Box sx={{ width: "100%" }}>
            <TabPanel value={tabValue} index={0}>
              <Container>
                <AgentReservationList />
              </Container>
            </TabPanel>
            <TabPanel value={tabValue} index={1}>
              <AgentRealEstate myRealEstate={myRealEstate} />
            </TabPanel>
          </Box>
        </Box>
      </Box>
    </>
  );
}
