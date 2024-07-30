import React from "react";
import RealEstateImages from "./RealEstateImages";
import RealEstateDescription from "./RealEstateDescription";
import { Button, Typography, Box, Divider } from "@mui/material";
import { useTheme } from "@mui/material/styles";

const RealEstateDetail = ({ item, onOpenReservationCard }) => {
  const theme = useTheme();

  const styles = {
    container: { display: "flex", flexDirection: "column", gap: 3 },
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
          {item.title}
          <Typography variant="subtitle1" color="text.secondary">
            Location: {item.location}
          </Typography>
          <Divider variant="middle" sx={{ margin: 2 }} />
        </Typography>
        <RealEstateDescription />
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
