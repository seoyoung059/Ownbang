import React from "react";
import RealEstateImages from "./RealEstateImages";
import RealEstateDescription from "./RealEstateDescription";
import AgentInfo from "./AgentInfo";
import { Button, Typography, Box, Divider } from "@mui/material";
import { useTheme } from "@mui/material/styles";

const RealEstateDetail = ({
  item,
  onOpenReservationCard,
  isAuthenticated, // Correct prop name
  user,
}) => {
  const theme = useTheme();

  // item과 item.roomImageResponses가 존재하는지 확인 후, 이미지 URL 배열 생성
  const images =
    item && item.roomImageResponses
      ? item.roomImageResponses.map((image) => image.roomImageUrl)
      : [];

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
        {/* RealEstateImages에 이미지 배열 전달 */}
        <RealEstateImages images={images} />
        <Typography variant="h5" sx={styles.title}>
          {item.dealType} {item.deposit}/{item.monthlyRent}
          <Typography variant="subtitle2" color="text.secondary">
            Location: {item.parcel}
          </Typography>
          <Divider variant="middle" sx={{ margin: 2 }} />
        </Typography>
        <RealEstateDescription item={item} />
      </Box>

      <Box>
        <AgentInfo item={item} />
      </Box>

      {isAuthenticated && !user.isAgent && (
        <Box sx={{ display: "flex", flexDirection: "column" }}>
          <Button
            variant="contained"
            sx={styles.button}
            onClick={onOpenReservationCard}
          >
            예약하기
          </Button>
        </Box>
      )}
    </Box>
  );
};

export default RealEstateDetail;
