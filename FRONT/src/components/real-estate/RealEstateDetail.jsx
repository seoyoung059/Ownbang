import React from "react";
import RealEstateImages from "./RealEstateImages";
import RealEstateDescription from "./RealEstateDescription";
import AgentInfo from "./AgentInfo";
import { Button, Typography, Box, Divider } from "@mui/material";
import { useTheme } from "@mui/material/styles";

const RealEstateDetail = ({ item, onOpenReservationCard }) => {
  const theme = useTheme();

  const styles = {
    container: { display: "flex", flexDirection: "column", gap: 1 },
    title: { marginTop: 4 },
    button: {
      marginTop: "16px",
      borderRadius: "7px",
      padding: "13px 15px",
      backgroundColor: theme.palette.primary.dark,
      fontSize: theme.typography.fontSize,
    },
  };

  return (
    <Box sx={styles.container}>
      <Box sx={{ marginTop: 2 }}>
        <RealEstateImages />
        <Typography variant="h4" sx={styles.title}>
          {item.dealType} {item.deposit}/{item.monthlyRent}
          <Typography variant="subtitle1" color="text.secondary">
            Location: {item.parcel}
          </Typography>
          <Divider variant="middle" sx={{ margin: 2 }} />
        </Typography>
        <RealEstateDescription item={item} />
      </Box>

      <Box>
        <AgentInfo item={item} />
      </Box>

      <Box sx={{ display: "flex", flexDirection: "column" }}>
        <Button
          variant="contained"
          sx={styles.button}
          onClick={onOpenReservationCard}
        >
          예약하기
        </Button>
      </Box>
    </Box>
  );
};

export default RealEstateDetail;
