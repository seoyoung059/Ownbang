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
            sx={{
              fontWeight: "bold",
              mb: 6,
              ml: 2,
              wordBreak: "keep-all",
              whiteSpace: "normal",
            }}
          >
            <span style={{ color: theme.palette.primary.main }}>온방</span>이
            제공하는 서비스를 확인해보세요!🔎
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
            <Card sx={{ width: 310, bgcolor: "transparent" }}>
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
                  image="https://cdn.pixabay.com/photo/2015/08/28/12/46/checklist-911840_960_720.png"
                  alt="체크 리스트"
                />
              </CardContent>
            </Card>
            <Card sx={{ width: 310, bgcolor: "transparent" }}>
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
                  image="https://cdn.pixabay.com/photo/2020/09/30/09/36/phone-5615121_1280.png"
                  alt="중개인과 화상통화"
                />
              </CardContent>
            </Card>
            <Card sx={{ width: 310, bgcolor: "transparent" }}>
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
                  image="https://img.freepik.com/free-vector/home-interior_23-2148000675.jpg?size=338&ext=jpg&ga=GA1.1.1788614524.1719446400&semt=ais_user"
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
            sx={{
              fontWeight: "bold",
              mb: 6,
              mr: 2,
              wordBreak: "keep-all",
              whiteSpace: "normal",
            }}
          >
            검색하고 근처{" "}
            <span style={{ color: theme.palette.primary.main }}>원룸</span>을
            확인해보세요!📷
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
            <Card sx={{ width: 310, bgcolor: theme.palette.common.white }}>
              <CardMedia
                component="img"
                height="280"
                image="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSg8sSKPnUA5GrLzWRlaBYcQyKig_HfNsBJuQ&s"
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
                  English Available <br />
                  역삼동 가격파괴자 : 넓고 가성비
                </Typography>
              </CardContent>
            </Card>

            <Card sx={{ width: 310, bgcolor: theme.palette.common.white }}>
              <CardMedia
                component="img"
                height="280"
                image="https://contents-cdn.viewus.co.kr/image/230131/443dd20c-a1a1-4464-8889-92860bb2a33d.jpeg"
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
                  고시생들을 위한 <br />
                  1년 계약 매물 즉시 입주 가능
                </Typography>
              </CardContent>
            </Card>
            <Card sx={{ width: 310, bgcolor: theme.palette.common.white }}>
              <CardMedia
                component="img"
                height="280"
                image="https://www.chosun.com/resizer/v2/NVGQZWFFYVHQZBH7PRVY3RZWZ4.jpg?auth=1c72966a647184331c0448844b68f922d217b6f1612a29a7604032faceb27e2e&width=616"
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
