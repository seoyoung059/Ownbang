import React, { useState, useEffect } from "react";
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
import { useTheme } from "@mui/material";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import { useBoundStore } from "../../store/store";
import TimePicker from "./TimePicker";

export default function StatusChangeForm({ toggleEdit, isAgent }) {
  const theme = useTheme();
  const navigate = useNavigate();

  const { upgradeAgent, getMyAgentInfo, modifyMyAgentInfo } = useBoundStore(
    (state) => ({
      upgradeAgent: state.upgradeAgent,
      getMyAgentInfo: state.getMyAgentInfo,
      modifyMyAgentInfo: state.modifyMyAgentInfo,
    })
  );

  // 유저 정보
  const [userInfo, setUserInfo] = useState({
    officeNumber: "",
    licenseNumber: "",
    officeAddress: "",
    detailOfficeAddress: "",
    officeName: "",
    weekendStartTime: "",
    weekendEndTime: "",
    weekdayStartTime: "",
    weekdayEndTime: "",
  });

  const [open, setOpen] = useState(false);
  const [errMsg, setErrMsg] = useState("");
  const [greetings, setGreetings] = useState("");

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleAddress = (el) => {
    setUserInfo((prevUserInfo) => ({
      ...prevUserInfo,
      officeAddress: el || "",
    }));
    handleClose();
  };

  // 전화번호 포맷팅 함수
  const formatPhoneNumber = (phoneNumber) => {
    let formattedPhoneNumber = phoneNumber.replace(/[^\d]/g, ""); // 숫자 이외의 문자 모두 제거

    if (formattedPhoneNumber.startsWith("02")) {
      // 02로 시작하는 경우 (02-XXXX-XXXX)
      if (formattedPhoneNumber.length > 10) {
        formattedPhoneNumber = formattedPhoneNumber.slice(0, 10); // 길이 제한
      }
      formattedPhoneNumber = formattedPhoneNumber.replace(
        /(\d{2})(\d{4})(\d{4})/,
        "$1-$2-$3"
      );
    } else {
      // 그 외 지역번호 (XXX-XXXX-XXXX)
      if (formattedPhoneNumber.length > 11) {
        formattedPhoneNumber = formattedPhoneNumber.slice(0, 11); // 길이 제한
      }
      formattedPhoneNumber = formattedPhoneNumber.replace(
        /(\d{3})(\d{4})(\d{4})/,
        "$1-$2-$3"
      );
    }

    return formattedPhoneNumber;
  };

  useEffect(() => {
    const fetchAgentInfo = async () => {
      try {
        const res = await getMyAgentInfo();
        if (res) {
          setUserInfo({
            officeNumber: formatPhoneNumber(res.officeNumber || ""),
            licenseNumber: res.licenseNumber || "",
            officeAddress: res.officeAddress || "",
            detailOfficeAddress: res.detailOfficeAddress || "",
            officeName: res.officeName || "",
            weekendStartTime: res.weekendStartTime || "",
            weekendEndTime: res.weekendEndTime || "",
            weekdayStartTime: res.weekdayStartTime || "",
            weekdayEndTime: res.weekdayEndTime || "",
          });
          setGreetings(res.greetings || "");
        }
      } catch (err) {
        console.error("Failed to load agent information", err);
      }
    };

    if (isAgent) {
      fetchAgentInfo();
    }
  }, [getMyAgentInfo, isAgent]);

  // 입력창에 있는 정보들을 userInfo에 반영
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserInfo((prevUserInfo) => ({
      ...prevUserInfo,
      [name]: value || "",
    }));
  };

  // 전화번호 입력 시 포맷팅 적용
  const handleCallNumberChange = (event) => {
    const formattedPhoneNumber = formatPhoneNumber(event.target.value);
    setUserInfo((prevUserInfo) => ({
      ...prevUserInfo,
      officeNumber: formattedPhoneNumber || "",
    }));
  };

  const handleNumberChange = (event) => {
    setUserInfo((prevUserInfo) => ({
      ...prevUserInfo,
      licenseNumber: event.target.value.replace(/[^\d]/g, "") || "",
    }));
  };

  const handleGreetingsChange = (event) => {
    setGreetings(event.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      let res;
      if (isAgent) {
        // 수정 기능 수행

        const req = {
          ...userInfo,
          greetings: greetings, // 이 부분 수정
          officeNumber: userInfo.officeNumber.split("-").join(""),
        };

        console.log(req);
        res = await modifyMyAgentInfo(req)
          .then()
          .catch((err) => console.error(err));
      } else {
        // 중개인 회원 전환 신청
        const req = {
          ...userInfo,
          officeNumber: userInfo.officeNumber.split("-").join(""),
        };
        console.log(req);
        res = await upgradeAgent(req);
      }

      if (res.resultCode === "SUCCESS") {
        toast.info(
          isAgent ? "중개인 정보 수정 완료." : "중개인 회원전환 신청 완료.",
          {
            position: "bottom-left",
            autoClose: 2000,
            hideProgressBar: true,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "light",
          }
        );
        navigate("/");
      }
    } catch (err) {
      setErrMsg(err.response?.data?.message || "An error occurred");
    }
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
            {isAgent ? "중개인 정보 수정" : "중개인 회원 전환"}
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
                    maxLength: 13, // 최대 입력 길이 설정 (예: 02-1234-5678 또는 043-1234-5678)
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
                  name="detailOfficeAddress"
                  value={userInfo.detailOfficeAddress}
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
              <Grid item alignItems="center" xs={12} sx={{ display: "flex" }}>
                <TimePicker
                  handleInputChange={handleInputChange}
                  userInfo={userInfo}
                />
              </Grid>
              {isAgent && (
                <Grid item xs={12}>
                  <TextField
                    label="사무실 소개"
                    name="greetings"
                    value={greetings}
                    onChange={handleGreetingsChange} // 이 부분 수정
                    fullWidth
                  />
                </Grid>
              )}

              <Grid item xs={12} sx={{ textAlign: "end" }}>
                <Typography
                  sx={{
                    fontSize: theme.fontSize.small,
                    color: theme.palette.error.main,
                  }}
                >
                  {errMsg}
                </Typography>
              </Grid>
            </Grid>
            <Grid
              item
              xs={8}
              sx={{ display: "flex", justifyContent: "space-around" }}
            >
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
                {isAgent ? "수정" : "확인"}
              </Button>
            </Grid>
          </Box>
        </Box>
      </Container>
    </>
  );
}
