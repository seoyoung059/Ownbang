// 중개인 정보가 담긴 컴퍼넌트
import { Rating, Typography, Box, Divider, ImageListItem } from "@mui/material";
import { useTheme } from "@mui/material";

const defaultProfileImage = "https://via.placeholder.com/150"; // 실제 이미지 URL 예시
const AgentStarRate = 4;

const AgentInfo = () => {
  const profileImage = defaultProfileImage;
  const theme = useTheme();

  return (
    <>
      <Divider variant="middle" sx={{ margin: 2 }} />
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          padding: 1,
          gap: 2,
        }}
      >
        <Typography variant="h6">중개인 정보</Typography>
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
              <img src={profileImage} alt="Agent Profile" />
            </ImageListItem>

            <Box sx={{ display: "flex", flexDirection: "column" }}>
              <Typography>김준영의 행복덕방</Typography>
              <Box sx={{ display: "flex", alignItems: "center" }}>
                <Rating
                  name="read-only-rating"
                  value={AgentStarRate}
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
            "역삼의 모든 원룸은 저를 통합니다."
          </Typography>
        </Box>
      </Box>
    </>
  );
};

export default AgentInfo;
