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
  const formatCurrency = (amount) => {
    return (amount / 10000).toLocaleString(); // 1000으로 나누고 천 단위로 포맷팅
  };

  return (
    <Box sx={styles.container}>
      <Box sx={{ marginTop: 2 }}>
        <RealEstateImages />
        <Typography variant="h4" sx={styles.title}>
          {item.dealType} {formatCurrency(item.deposit)}/
          {formatCurrency(item.monthlyRent)}
          <Typography variant="subtitle1" color="text.secondary">
            Location: {item.parcel}
          </Typography>
          <Divider variant="middle" sx={{ margin: 2 }} />
        </Typography>
        <RealEstateDescription item={item} />
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
