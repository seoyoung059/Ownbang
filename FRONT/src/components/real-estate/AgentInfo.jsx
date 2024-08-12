import { Rating, Typography, Box, Divider, ImageListItem } from "@mui/material";
import { useTheme } from "@mui/material";

const AgentInfo = ({ item }) => {
  const { agentResponse } = item;
  const theme = useTheme();

  if (!agentResponse) {
    return null;
  }

  return (
    <>
      <Divider variant="middle" sx={{ margin: 1 }} />
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          padding: 1,
          gap: 1,
        }}
      >
        <Typography variant="subtitle1">중개인 정보</Typography>
        <Box
          title="info"
          sx={{
            padding: 3,
            backgroundColor: theme.palette.background.paper,
            boxShadow: 1,
            borderRadius: 1,
          }}
        >
          <Box
            sx={{
              display: "flex",
              flexDirection: "row",
              gap: 3,
              alignItems: "center",
            }}
          >
            <ImageListItem
              sx={{
                width: 80,
                height: 80,
                "& img": {
                  width: "100%",
                  height: "100%",
                  objectFit: "cover",
                  borderRadius: "50px",
                },
              }}
            >
              <img src={agentResponse.profileImage} alt="Agent Profile" />
            </ImageListItem>

            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <Typography variant="subtitle1" sx={{ padding: 1 }}>
                {agentResponse.officeName}
              </Typography>
              <Box sx={{ display: "flex", alignItems: "center" }}>
                <Rating
                  name="read-only-rating"
                  value={agentResponse.starRating}
                  readOnly
                  sx={{ color: theme.palette.bookmark }}
                />
              </Box>
            </Box>
          </Box>
          <Typography
            variant="body2"
            sx={{ marginTop: "20px", color: theme.palette.text.secondary }}
          >
            {agentResponse.greeting}
          </Typography>
        </Box>
      </Box>
    </>
  );
};

export default AgentInfo;
