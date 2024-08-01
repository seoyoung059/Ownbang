import React, { useState } from "react";
import {
  Box,
  Button,
  Typography,
  TextField,
  Card,
  CardContent,
  CardMedia,
  Container,
  InputAdornment,
  useTheme,
  useMediaQuery,
} from "@mui/material";
import RealEstateSearchBar from "../components/real-estate/RealEstateSearchBar";
import { useBoundStore } from "../store/store";

export default function MainPage() {
  const { searchTerm, setSearchTerm } = useBoundStore((state) => ({
    searchTerm: state.searchTerm,
    setSearchTerm: state.setSearchTerm,
  }));
  const theme = useTheme();
  const isLg = useMediaQuery(theme.breakpoints.down("lg"));
  const isSm = useMediaQuery(theme.breakpoints.down("sm"));

  const onSearch = (term) => {
    setSearchTerm(term);
  };

  return (
    <Container sx={{ mt: 32 }}>
      <Box sx={{ textAlign: "center", mt: 4 }}>
        <Typography
          variant="h2"
          component="div"
          sx={{ mb: 6, fontFamily: theme.title.fontFamily, fontSize: 64 }}
        >
          온방
        </Typography>
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            mb: 8,
          }}
        >
          <RealEstateSearchBar onSearch={onSearch} isMain={true} />
        </Box>
      </Box>

      <Box
        sx={{
          display: "flex",
          flexDirection: isLg ? "column" : "row",
          justifyContent: "center",
          alignItems: isLg ? "center" : "flex-start",
          mt: 16,
          mb: 4,
          gap: 4,
        }}
      >
        {/* 왼쪽 섹션 */}
        <Box
          sx={{
            flex: 1,
            textAlign: isLg ? "center" : "left",
            mb: isLg ? 4 : 0,
          }}
        >
          <Typography
            variant="h6"
            component="div"
            sx={{ fontWeight: "bold", mb: 2, ml: 2 }}
          >
            <span style={{ color: theme.palette.primary.main }}>온방</span>이
            제공하는 서비스를 확인해보세요! 🔎
          </Typography>
          <Box
            sx={{
              display: "flex",
              flexDirection: isSm ? "column" : "row",
              justifyContent: "space-between",
              alignItems: isSm ? "center" : "flex-start",
              mb: 4,
              gap: 2,
            }}
          >
            <Card sx={{ width: 300, bgcolor: theme.palette.common.white }}>
              <CardContent>
                <Typography
                  gutterBottom
                  variant="h5"
                  component="div"
                  sx={{ textAlign: "center" }}
                >
                  체크 리스트
                </Typography>
                <CardMedia
                  component="img"
                  height="260"
                  image="https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA0MTZfMjYz%2FMDAxNjgxNjI0NDU2OTc4.QB426iKZQVIeAbkpBUuLygz8ouzLtElqqRdDblYpIk0g.42_Ef4d4hiRqIcA6E_my_7x2oMUYER78Fe1VHPjfImIg.JPEG.miiiiij%2F%25B8%25DB.JPG&type=sc960_832"
                  alt="체크 리스트"
                />
              </CardContent>
            </Card>
            <Card sx={{ width: 300, bgcolor: theme.palette.common.white }}>
              <CardContent>
                <Typography
                  gutterBottom
                  variant="h5"
                  component="div"
                  sx={{ textAlign: "center" }}
                >
                  중개인과 화상통화
                </Typography>
                <CardMedia
                  component="img"
                  height="260"
                  image="https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F5646%2F2021%2F11%2F02%2F0000028034_001_20220318181605686.jpeg&type=a340"
                  alt="중개인과 화상통화"
                />
              </CardContent>
            </Card>
          </Box>
        </Box>

        {/* 오른쪽 섹션 */}
        <Box sx={{ flex: 1, textAlign: isLg ? "center" : "left" }}>
          <Typography
            variant="h6"
            component="div"
            sx={{ fontWeight: "bold", mb: 2, ml: 2 }}
          >
            <span style={{ color: theme.palette.primary.main }}>역삼동</span>{" "}
            근처 원룸을 확인해보세요! 📷
          </Typography>
          <Box
            sx={{
              display: "flex",
              flexDirection: isSm ? "column" : "row",
              justifyContent: "space-between",
              alignItems: isSm ? "center" : "flex-start",
              mb: 4,
              gap: 2,
            }}
          >
            <Card sx={{ width: 300, bgcolor: theme.palette.common.white }}>
              <CardMedia
                component="img"
                height="200"
                image="https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMjEyMDFfMTQ5%2FMDAxNjY5ODg0OTI4MDUz.5ESSkyfUD2yjr5kZR_SFnkyI51elH5edIV1uRAloNuEg.tGvq392VplgscDcK1Ivr_uo1PfnMKAgbXOL3f4RHk2Ag.JPEG.star3qp%2F20221201%25A3%25DF142507.jpg&type=sc960_832"
                alt="원룸"
              />
              <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                  월세 1000/62
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  반지하, 25㎡, 관리비 10만
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  English Available 역삼동 가격파괴자 넓고 가성비
                </Typography>
              </CardContent>
            </Card>

            <Card sx={{ width: 300, bgcolor: theme.palette.common.white }}>
              <CardMedia
                component="img"
                height="200"
                image="https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA5MzBfMjY5%2FMDAxNjk2MDU1NzY0Mjc5.mIUtkhCZRZQ8J83-aYJv9dj9BeJm7_F9YkJ7_vgcijsg.9ynSg81rBHJrVR8SjHwBaFrsA5XxEorhsl9php0IEMEg.JPEG.star3qp%2F20230923%25A3%25DF100156.jpg&type=a340"
                alt="원룸"
              />
              <CardContent>
                <Typography gutterBottom variant="h5" component="div">
                  월세 500/80
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  2층, 25㎡, 관리비 10만
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  SSAFY 교육생들만을 위한 1년 계약 매물 즉시 입주 가능
                </Typography>
              </CardContent>
            </Card>
          </Box>
        </Box>
      </Box>
    </Container>
  );
}
