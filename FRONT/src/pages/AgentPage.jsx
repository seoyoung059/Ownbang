import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Box, Button, Divider, Container } from "@mui/material";
import { useTheme } from "@mui/material";
import AgentRealEstate from "../components/user-info/AgentRealEstate";
import AgentReservationList from "../components/user-info/AgentReservationList";

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
              <AgentRealEstate />
            </TabPanel>
          </Box>
        </Box>
      </Box>
    </>
  );
}
