import React, { useState } from "react";
import {
  Container,
  CssBaseline,
  Box,
  Typography,
  Divider,
  Grid,
  TextField,
  Button,
  Modal,
} from "@mui/material";
import AddressSearch from "../common/AddressSearch";
import { useTheme } from "@emotion/react";

// 더미데이터 데이터 연결 필요
const user = {
  userName: "김일태",
  phoneNumber: "010-1234-5678",
  userId: "iltae94@gmail.com",
  password: "password123",
};

export default function UserInfoEditForm({ toggleEdit }) {
  const theme = useTheme();
  // 유저 정보
  const [userInfo, setUserInfo] = useState({
    officeNumber: "",
    licenseNumber: "",
    officeAddress: "",
    officeDetailAddress: "",
    officeName: "",
  });

  const [open, setOpen] = useState(false);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleAddress = (el) => {
    setUserInfo(() => ({
      ...userInfo,
      officeAddress: el,
    }));
    handleClose();
  };

  // 입력창에 있는 정보들을 userInfo에 반영
  // input의 name 속성과 value를 엮었기에 input의 name과 유저 정보 객체 키와 연결
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserInfo((prevUserInfo) => ({
      ...prevUserInfo,
      [name]: value,
    }));
  };

  // 전화번호
  const handleCallNumberChange = (event) => {
    let formattedPhoneNumber = event.target.value.replace(/[^\d]/g, ""); // 숫자 이외의 문자 모두 제거

    // '-' 기호 삽입
    if (formattedPhoneNumber.length > 2 && formattedPhoneNumber.length <= 6) {
      formattedPhoneNumber = formattedPhoneNumber.replace(
        /(\d{3})(\d{4})/,
        "$1-$2"
      );
    } else if (formattedPhoneNumber.length > 6) {
      formattedPhoneNumber = formattedPhoneNumber.replace(
        /(\d{2})(\d{4})(\d{4})/,
        "$1-$2-$3"
      );
    }

    setUserInfo((prevUserInfo) => ({
      ...prevUserInfo,
      officeNumber: formattedPhoneNumber,
    }));
  };

  const handleNumberChange = (event) => {
    setUserInfo((prevUserInfo) => ({
      ...prevUserInfo,
      licenseNumber: event.target.value.replace(/[^\d]/g, ""),
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // 일단 정보 출력 - 나중엔 api post
    console.log("중개인 정보:", userInfo);
  };

  return (
    <>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: "flex",
            flexDirection: "column",
          }}
        >
          <Typography
            sx={{ mt: 12, pl: 2, fontWeight: theme.fontWeight.bold }}
            component="h1"
            variant="h5"
          >
            중개인 회원 전환
          </Typography>
          <Divider component="div" sx={{ mt: 1, width: "100%" }} />
          <Box component="form" noValidate sx={{ mt: 4 }}>
            <Grid container spacing={4}>
              <Grid item xs={12}>
                <TextField
                  label="사무실 번호"
                  id="officeNumber"
                  name="officeNumber"
                  value={userInfo.officeNumber}
                  onChange={(handleInputChange, handleCallNumberChange)}
                  fullWidth
                  inputProps={{
                    maxLength: 12, // 최대 입력 길이 설정 (예: 02-1234-5678)
                  }}
                  placeholder="02-1234-5678"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="자격 번호"
                  type="text"
                  id="licenseNumber"
                  name="licenseNumber"
                  value={userInfo.licenseNumber}
                  onChange={(handleInputChange, handleNumberChange)}
                />
              </Grid>

              <Modal open={open} onClose={handleClose}>
                <Box
                  sx={{
                    position: "absolute",
                    top: "52%",
                    left: "50%",
                    transform: "translate(-50%, -50%)",
                    width: 400,
                    height: 580,
                    bgcolor: "background.paper",
                    border: `2px solid ${theme.palette.common.grey}`,
                    boxShadow: 24,
                    p: 4,
                  }}
                >
                  <AddressSearch handleAddress={handleAddress} />
                </Box>
              </Modal>

              <Grid item xs={10}>
                <TextField
                  label="사무실 주소"
                  value={userInfo.officeAddress}
                  onChange={handleInputChange}
                  fullWidth
                />
              </Grid>
              <Grid
                item
                xs={2}
                sx={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                <Button onClick={handleOpen}>검색</Button>
              </Grid>
              <Grid item xs={12}>
                <TextField
                  label="상세 주소"
                  name="officeDetailAddress"
                  value={userInfo.officeDetailAddress}
                  onChange={handleInputChange}
                  fullWidth
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  label="사무실 이름"
                  name="officeName"
                  value={userInfo.officeName}
                  onChange={handleInputChange}
                  fullWidth
                />
              </Grid>
            </Grid>
            <Grid
              item
              xs={8}
              sx={{ display: "flex", justifyContent: "space-around" }}
            >
              {/* 취소 버튼 클릭 시 어느 페이지로 이동할 지 */}
              <Button
                variant="contained"
                onClick={() => toggleEdit(false)}
                sx={{
                  mt: 6,
                  mb: 2,
                  height: "50px",
                  width: "25%",
                  backgroundColor: theme.palette.action.disabled,
                }}
              >
                취소
              </Button>
              <Button
                type="submit"
                variant="contained"
                onClick={handleSubmit}
                sx={{
                  mt: 6,
                  mb: 2,
                  height: "50px",
                  width: "25%",
                  backgroundColor: theme.palette.primary.main,
                }}
              >
                확인
              </Button>
            </Grid>
          </Box>
        </Box>
      </Container>
    </>
  );
}
