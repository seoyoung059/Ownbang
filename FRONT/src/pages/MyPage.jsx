import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Box, Button, Divider } from "@mui/material";
import { useTheme } from "@mui/material";
import MyCheckList from "../components/user-info/MyCheckList";
import MyBookmarkList from "../components/user-info/MyBookmarkList";
import MyReservationList from "../components/user-info/MyReservationList";
import CheckList from "../components/checklist/CheckList";

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

export default function Mypage() {
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
    <Container sx={{ mt: 16 }}>
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
          예약 목록
        </Button>
        <Divider orientation="vertical" flexItem />
        <Button
          variant={tabValue === 1 ? "contained" : "outlined"}
          size="large"
          onClick={() => handleChange(1)}
          sx={{ mx: 1 }}
        >
          찜한 목록
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
            <MyReservationList />
          </TabPanel>
          <TabPanel value={tabValue} index={1}>
            <MyBookmarkList />
          </TabPanel>
        </Box>
      </Box>
    </Container>
  );
}
