import React, { useRef, useEffect, useState } from "react";
import {
  Box,
  Typography,
  Card,
  CardContent,
  CardMedia,
  Container,
  useTheme,
  useMediaQuery,
} from "@mui/material";
import RealEstateSearchBar from "../components/real-estate/RealEstateSearchBar";
import { useBoundStore } from "../store/store";

export default function MainPage() {
  const mainSection = useRef(null);
  const leftSection = useRef(null);
  const rightSection = useRef(null);
  const sectionRefs = [mainSection, leftSection, rightSection];
  const { searchTerm, setSearchTerm } = useBoundStore((state) => ({
    searchTerm: state.searchTerm,
    setSearchTerm: state.setSearchTerm,
  }));
  const theme = useTheme();
  const isLg = useMediaQuery(theme.breakpoints.down("lg"));
  const isMd = useMediaQuery(theme.breakpoints.down("md"));

  const onSearch = (term) => {
    setSearchTerm(term);
  };

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add("slide-in");
          } else {
            entry.target.classList.remove("slide-in");
          }
        });
      },
      { threshold: 0.1 }
    );

    sectionRefs.forEach((ref) => {
      if (ref.current) {
        observer.observe(ref.current);
      }
    });

    return () => {
      sectionRefs.forEach((ref) => {
        if (ref.current) {
          observer.unobserve(ref.current);
        }
      });
    };
  }, [sectionRefs]);

  return (
    <>
      <Box sx={{ textAlign: "center", mt: 36 }} ref={mainSection}>
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
            mb: 48,
          }}
        >
          <RealEstateSearchBar onSearch={onSearch} isMain={true} />
        </Box>
      </Box>
      {/* 왼쪽 섹션 */}
      <Box
        sx={{
          width: "100%",
          backgroundColor: "#f5f5f5",
          display: "flex",
          justifyContent: "center",
          overflowX: "hidden", // 가로 스크롤바를 숨깁니다.
        }}
      >
        <Box
          ref={leftSection}
          sx={{
            flex: 1,
            textAlign: isMd ? "center" : "left",
            mb: isLg ? 12 : 28,
            mt: 16,
            ml: !isLg && 24,
            opacity: 0,
            transform: "translateX(-50%)",
            transition: "transform 0.5s ease-out, opacity 0.5s ease-out",
            "&.slide-in": {
              opacity: 1,
              transform: "translateX(0)",
            },
          }}
        >
          <Typography
            variant="h5"
            component="div"
            sx={{ fontWeight: "bold", mb: 6, ml: 2 }}
          >
            <span style={{ color: theme.palette.primary.main }}>온방</span>이
            제공하는 서비스를 확인해보세요! 🔎
          </Typography>
          <Box
            sx={{
              display: "flex",
              flexDirection: isMd ? "column" : "row",
              alignItems: isMd ? "center" : "flex-start",
              mb: 4,
              gap: 4,
            }}
          >
            <Card sx={{ width: 320, bgcolor: "transparent" }}>
              <CardContent>
                <Typography
                  gutterBottom
                  variant="h6"
                  component="div"
                  sx={{ textAlign: "center" }}
                >
                  체크 리스트
                </Typography>
                <CardMedia
                  component="img"
                  height="360"
                  image="https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA0MTZfMjYz%2FMDAxNjgxNjI0NDU2OTc4.QB426iKZQVIeAbkpBUuLygz8ouzLtElqqRdDblYpIk0g.42_Ef4d4hiRqIcA6E_my_7x2oMUYER78Fe1VHPjfImIg.JPEG.miiiiij%2F%25B8%25DB.JPG&type=sc960_832"
                  alt="체크 리스트"
                />
              </CardContent>
            </Card>
            <Card sx={{ width: 320, bgcolor: "transparent" }}>
              <CardContent>
                <Typography
                  gutterBottom
                  variant="h6"
                  component="div"
                  sx={{ textAlign: "center" }}
                >
                  중개인과 화상통화
                </Typography>
                <CardMedia
                  component="img"
                  height="360"
                  image="https://search.pstatic.net/common/?src=http%3A%2F%2Fimgnews.naver.net%2Fimage%2F5646%2F2021%2F11%2F02%2F0000028034_001_20220318181605686.jpeg&type=a340"
                  alt="중개인과 화상통화"
                />
              </CardContent>
            </Card>
            <Card sx={{ width: 320, bgcolor: "transparent" }}>
              <CardContent>
                <Typography
                  gutterBottom
                  variant="h6"
                  component="div"
                  sx={{ textAlign: "center" }}
                >
                  녹화 다시보기
                </Typography>
                <CardMedia
                  component="img"
                  height="360"
                  image="https://mblogthumb-phinf.pstatic.net/MjAxNzA2MTZfMjY5/MDAxNDk3NTY5NzU5MjAz.1Y2vcmi5t2sndtAVA-CNIMnOFuGWC-kF7Ns2DEIscJcg.X765XjvAG1424W3IKIiGVcXyjPTwWPeThTsOYkGn_T4g.JPEG.threenitro/%EB%B0%A9%EA%B5%AC%EA%B2%BD%ED%95%98%EA%B8%B0_%281%29.JPG?type=w800"
                  alt="녹화 다시보기"
                />
              </CardContent>
            </Card>
          </Box>
        </Box>
      </Box>

      {/* 오른쪽 섹션 */}
      <Box
        sx={{
          width: "100%",
          display: "flex",
          justifyContent: "center",
          overflowX: "hidden", // 가로 스크롤바를 숨깁니다.
        }}
      >
        <Box
          ref={rightSection}
          sx={{
            flex: 1,
            textAlign: isMd ? "center" : "right",
            mb: isLg ? 12 : 28,
            mt: 16,
            mr: !isLg && 24,
            opacity: 0,
            transform: "translateX(50%)",
            transition: "transform 0.5s ease-out, opacity 0.5s ease-out",
            "&.slide-in": {
              opacity: 1,
              transform: "translateX(0)",
            },
          }}
        >
          <Typography
            variant="h5"
            component="div"
            sx={{ fontWeight: "bold", mb: 6, mr: 2 }}
          >
            검색하고 근처{" "}
            <span style={{ color: theme.palette.primary.main }}>원룸</span>을
            확인해보세요! 📷
          </Typography>
          <Box
            sx={{
              display: "flex",
              flexDirection: isMd ? "column" : "row",
              alignItems: isMd ? "center" : "flex-start",
              justifyContent: isMd ? "center" : "right",
              mb: 4,
              gap: 4,
            }}
          >
            <Card sx={{ width: 320, bgcolor: theme.palette.common.white }}>
              <CardMedia
                component="img"
                height="280"
                image="https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMjEyMDFfMTQ5%2FMDAxNjY5ODg0OTI4MDUz.5ESSkyfUD2yjr5kZR_SFnkyI51elH5edIV1uRAloNuEg.tGvq392VplgscDcK1Ivr_uo1PfnMKAgbXOL3f4RHk2Ag.JPEG.star3qp%2F20221201%25A3%25DF142507.jpg&type=sc960_832"
                alt="원룸"
              />
              <CardContent>
                <Typography gutterBottom variant="h6" component="div">
                  월세 1000/62
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  반지하, 25㎡, 관리비 10만
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  English Available 역삼동 가격파괴자 <br />
                  넓고 가성비
                </Typography>
              </CardContent>
            </Card>

            <Card sx={{ width: 320, bgcolor: theme.palette.common.white }}>
              <CardMedia
                component="img"
                height="280"
                image="https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzA5MzBfMjY5%2FMDAxNjk2MDU1NzY0Mjc5.mIUtkhCZRZQ8J83-aYJv9dj9BeJm7_F9YkJ7_vgcijsg.9ynSg81rBHJrVR8SjHwBaFrsA5XxEorhsl9php0IEMEg.JPEG.star3qp%2F20230923%25A3%25DF100156.jpg&type=a340"
                alt="원룸"
              />
              <CardContent>
                <Typography gutterBottom variant="h6" component="div">
                  월세 500/80
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  2층, 25㎡, 관리비 10만
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  SSAFY 교육생들만을 위한 <br />
                  1년 계약 매물 즉시 입주 가능
                </Typography>
              </CardContent>
            </Card>
            <Card sx={{ width: 320, bgcolor: theme.palette.common.white }}>
              <CardMedia
                component="img"
                height="280"
                image="https://cdn.ggumim.co.kr/cache/star/600/76e8aa01-6ecc-4cef-9122-47cfb38d71dd.jpg"
                alt="원룸"
              />
              <CardContent>
                <Typography gutterBottom variant="h6" component="div">
                  월세 100/30
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  1층, 15㎡, 관리비 5만
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  사장님이 미쳤어요!! <br />
                  리모델링 초특가 매물!! 🎈🎈
                </Typography>
              </CardContent>
            </Card>
          </Box>
        </Box>
      </Box>
    </>
  );
}
