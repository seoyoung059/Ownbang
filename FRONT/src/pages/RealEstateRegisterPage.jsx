import React, { useState, useEffect } from "react";
import {
  Container,
  Typography,
  Box,
  Divider,
  TextField,
  Button,
  Grid,
  MenuItem,
  Checkbox,
  FormControlLabel,
  Modal,
  Input,
  IconButton,
  Snackbar,
  Alert,
} from "@mui/material";
import { Delete } from "@mui/icons-material";
import { useTheme } from "@mui/material";
import AddressSearch from "../components/common/AddressSearch";
import RealEstateMap from "../components/real-estate/RealEstateMap";
import { useBoundStore } from "../store/store";
import { useNavigate } from "react-router-dom";
import KakaoMapSearch from "../components/real-estate/KakaoMapsearch";

export default function RealEstateRegisterPage() {
  const navigate = useNavigate();
  const { makeRoom } = useBoundStore((state) => ({
    makeRoom: state.makeRoom,
  }));

  const [visibleMarkers, setVisibleMarkers] = useState([]);
  const theme = useTheme();
  const [address, setAddress] = useState("");
  const [parcel, setParcel] = useState("");
  const [coordinates, setCoordinates] = useState({ lat: null, lng: null });
  const [open, setOpen] = useState(false);
  const [selectedFiles, setSelectedFiles] = useState([]);

  const [isSubmitting, setIsSubmitting] = useState(false);

  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  const [errMsg, setErrMsg] = useState("");

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
    // 스낵바가 닫힐 때 페이지 이동
    if (snackbarMessage === "등록이 완료되었습니다!") {
      navigate("/");
    } else {
      setIsSubmitting(false);
    }
  };

  const [basicInfo, setBasicInfo] = useState({
    dealType: "",
    roomType: "",
    structure: "",
    isLoft: false,
    exclusiveArea: "",
    supplyArea: "",
    roomFloor: "",
    deposit: "",
    monthlyRent: "",
    maintenanceFee: "",
    detailAddress: "",
  });

  const [appliances, setAppliances] = useState({
    refrigerator: false,
    washingMachine: false,
    airConditioner: false,
    bed: false,
    desk: false,
    microwave: false,
    closet: false,
    chair: false,
  });

  const [roomDetails, setRoomDetails] = useState({
    roomCount: "",
    bathroomCount: "",
    heatingType: "",
    moveInDate: "",
    buildingFloor: "",
    elevator: false,
    totalParking: "",
    parking: "",
    approvalDate: "",
    firstRegistrationDate: "",
    facing: "",
    purpose: "",
  });

  const handleAddress = (newAddress, newParcel) => {
    setAddress(newAddress);
    setParcel(newParcel);
    handleClose();
  };

  const handleFileChange = (event) => {
    const files = Array.from(event.target.files);
    const validImageTypes = ["image/jpeg", "image/png", "image/jpg"];
    const invalidFiles = files.filter(
      (file) => !validImageTypes.includes(file.type)
    );

    if (invalidFiles.length > 0) {
      setSnackbarMessage("jpg, jpeg, png 파일만 업로드 가능합니다.");
      setSnackbarOpen(true);
    } else {
      const newFiles = files.filter(
        (file) =>
          !selectedFiles.some((selectedFile) => selectedFile.name === file.name)
      );
      setSelectedFiles([...selectedFiles, ...newFiles]);
    }
  };

  const handleFileRemove = (fileName) => {
    setSelectedFiles(selectedFiles.filter((file) => file.name !== fileName));
  };

  const handleApplianceChange = (event) => {
    setAppliances({
      ...appliances,
      [event.target.name]: event.target.checked,
    });
  };

  const handleRoomDetailChange = (event) => {
    const { name, value, type, checked } = event.target;
    setRoomDetails({
      ...roomDetails,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleBasicInfoChange = (event) => {
    const { name, value, type, checked } = event.target;
    setBasicInfo({
      ...basicInfo,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleNumberInput = (event) => {
    const value = event.target.value;
    event.target.value = value.replace(/[^0-9]/g, "");
  };

  const handleDecimalInput = (event) => {
    let value = event.target.value;
    value = value.replace(/[^0-9.]/g, "");
    if ((value.match(/\./g) || []).length > 1) {
      value = value.substring(0, value.length - 1);
    }
    const parts = value.split(".");
    if (parts[1] && parts[1].length > 3) {
      parts[1] = parts[1].substring(0, 3);
      value = parts.join(".");
    }
    if (parseFloat(value) > 1000) {
      value = "999";
    }
    event.target.value = value;
  };

  const onBoundsChange = (markers) => {
    setVisibleMarkers(markers);
  };

  useEffect(() => {
    if (!address) return;

    const geocoder = new kakao.maps.services.Geocoder();
    geocoder.addressSearch(address, (result, status) => {
      if (status === kakao.maps.services.Status.OK) {
        const coords = {
          lat: result[0].y,
          lng: result[0].x,
        };
        setCoordinates(coords);
      }
    });
  }, [address]);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleSubmit = async () => {
    if (isSubmitting) return;
    setIsSubmitting(true);
    const formData = new FormData();

    const roomDetailCreateRequest = { ...roomDetails };

    const roomCreateRequest = {
      ...basicInfo,
      latitude: coordinates.lat,
      longitude: coordinates.lng,
      parcel: parcel,
      road: address,
      roomAppliancesCreateRequest: appliances,
      roomDetailCreateRequest: roomDetailCreateRequest,
    };

    formData.append(
      "roomCreateRequest",
      new Blob([JSON.stringify(roomCreateRequest)], {
        type: "application/json",
      })
    );

    selectedFiles.forEach((file) => {
      formData.append("roomImageFiles", file);
    });

    try {
      await makeRoom(formData);
      setErrMsg("");
      setSnackbarMessage("등록이 완료되었습니다!");
      setSnackbarOpen(true);
    } catch (error) {
      console.log(error);
      if (error.response.data.status === 413) {
        setErrMsg("이미지 용량 초과입니다.");
      } else if (error.response.status === 400) {
        setErrMsg(error.response.data.data);
      }
      setIsSubmitting(false); // 실패 시 버튼 활성화
    }
  };

  return (
    <Container
      sx={{
        mt: 8,
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
      }}
      component="main"
      maxWidth="md"
    >
      <Box sx={{ mt: 12, pl: 2 }}>
        <Typography
          sx={{ fontWeight: theme.typography.fontWeightBold }}
          component="h1"
          variant="h5"
        >
          매물 등록
        </Typography>
      </Box>
      <Divider component={"div"} sx={{ mt: 1, width: "100%" }} />

      <Box component="form" sx={{ mt: 3 }}>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={4}>
            <TextField
              select
              fullWidth
              label="매물 종류"
              name="roomType"
              value={basicInfo.roomType}
              onChange={handleBasicInfoChange}
            >
              <MenuItem value="오피스텔">오피스텔</MenuItem>
              <MenuItem value="빌라">빌라</MenuItem>
            </TextField>
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              select
              fullWidth
              label="거래 종류"
              name="dealType"
              value={basicInfo.dealType}
              onChange={handleBasicInfoChange}
            >
              <MenuItem value="월세">월세</MenuItem>
              <MenuItem value="전세">전세</MenuItem>
              <MenuItem value="매매">매매</MenuItem>
            </TextField>
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              select
              fullWidth
              label="구조"
              name="structure"
              value={basicInfo.structure}
              onChange={handleBasicInfoChange}
            >
              <MenuItem value="원룸">원룸</MenuItem>
              <MenuItem value="투룸">투룸</MenuItem>
              <MenuItem value="쓰리룸">쓰리룸</MenuItem>
              <MenuItem value="분리형">분리형</MenuItem>
            </TextField>
          </Grid>
          <Grid item xs={10}>
            <TextField
              fullWidth
              label="주소"
              value={address}
              InputProps={{
                readOnly: true,
              }}
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
            <Button variant="contained" size="large" onClick={handleOpen}>
              검색
            </Button>
          </Grid>
          {address && (
            <>
              <Grid item xs={12} sm={6}>
                <Box sx={{ width: "100%", height: "280px" }}>
                  <KakaoMapSearch
                    searchTerm={address}
                    mark={coordinates}
                    onBoundsChange={onBoundsChange}
                    style={{ height: "100%" }}
                    size="100%"
                  />
                </Box>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Grid container spacing={2}>
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      label="상세 주소"
                      name="detailAddress"
                      value={basicInfo.detailAddress}
                      onChange={handleBasicInfoChange}
                      placeholder="추가 주소 정보를 입력하세요. (동, 호수 등)"
                    />
                  </Grid>
                  <Grid item xs={4}>
                    <TextField
                      fullWidth
                      label="건물 층"
                      name="buildingFloor"
                      value={roomDetails.buildingFloor}
                      onChange={handleRoomDetailChange}
                      onInput={handleNumberInput}
                    />
                  </Grid>
                  <Grid item xs={4}>
                    <TextField
                      fullWidth
                      label="해당 층"
                      name="roomFloor"
                      value={basicInfo.roomFloor}
                      onChange={handleBasicInfoChange}
                      onInput={handleNumberInput}
                    />
                  </Grid>
                  <Grid
                    item
                    xs={4}
                    sx={{ display: "flex", justifyContent: "center" }}
                  >
                    <FormControlLabel
                      control={
                        <Checkbox
                          checked={basicInfo.isLoft}
                          onChange={handleBasicInfoChange}
                          name="isLoft"
                        />
                      }
                      label="복층여부"
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      label="전용 면적"
                      name="exclusiveArea"
                      value={basicInfo.exclusiveArea}
                      onChange={handleBasicInfoChange}
                      onInput={handleDecimalInput}
                      placeholder="000.00㎡"
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      label="공급 면적"
                      name="supplyArea"
                      value={basicInfo.supplyArea}
                      onChange={handleBasicInfoChange}
                      onInput={handleDecimalInput}
                      placeholder="000.00㎡"
                    />
                  </Grid>
                </Grid>
              </Grid>
            </>
          )}
          {!address && (
            <>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="상세 주소"
                  name="detailAddress"
                  value={roomDetails.detailAddress}
                  onChange={handleRoomDetailChange}
                  placeholder="추가 주소 정보를 입력하세요. (동, 호수 등)"
                />
              </Grid>
              <Grid item xs={12} sm={5}>
                <TextField
                  fullWidth
                  label="건물 층"
                  name="buildingFloor"
                  value={roomDetails.buildingFloor}
                  onInput={handleNumberInput}
                  onChange={handleRoomDetailChange}
                />
              </Grid>
              <Grid item xs={12} sm={5}>
                <TextField
                  fullWidth
                  label="해당 층"
                  name="roomFloor"
                  value={basicInfo.roomFloor}
                  onChange={handleBasicInfoChange}
                  onInput={handleNumberInput}
                />
              </Grid>
              <Grid
                item
                xs={12}
                sm={2}
                sx={{ display: "flex", justifyContent: "center" }}
              >
                <FormControlLabel
                  control={
                    <Checkbox
                      checked={basicInfo.isLoft}
                      onChange={handleBasicInfoChange}
                      name="isLoft"
                    />
                  }
                  label="복층여부"
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="전용 면적"
                  name="exclusiveArea"
                  value={basicInfo.exclusiveArea}
                  onChange={handleBasicInfoChange}
                  onInput={handleDecimalInput}
                  placeholder="000.00㎡"
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="공급 면적"
                  name="supplyArea"
                  value={basicInfo.supplyArea}
                  onChange={handleBasicInfoChange}
                  onInput={handleDecimalInput}
                  placeholder="000.00㎡"
                />
              </Grid>
            </>
          )}
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="보증금"
              name="deposit"
              value={basicInfo.deposit}
              onChange={handleBasicInfoChange}
              onInput={handleNumberInput}
              InputProps={{
                endAdornment: (
                  <Typography sx={{ whiteSpace: "nowrap" }}>만원</Typography>
                ),
              }}
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="월세"
              name="monthlyRent"
              value={basicInfo.monthlyRent}
              onChange={handleBasicInfoChange}
              onInput={handleNumberInput}
              InputProps={{
                endAdornment: (
                  <Typography sx={{ whiteSpace: "nowrap" }}>만원</Typography>
                ),
              }}
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="관리비"
              name="maintenanceFee"
              value={basicInfo.maintenanceFee}
              onChange={handleBasicInfoChange}
              onInput={handleNumberInput}
              InputProps={{
                endAdornment: (
                  <Typography sx={{ whiteSpace: "nowrap" }}>만원</Typography>
                ),
              }}
            />
          </Grid>
          <Grid item xs={12} sx={{ mb: 3 }}>
            <Button variant="contained" component="label">
              부동산 매물 사진 등록
              <Input
                type="file"
                inputProps={{ multiple: true, accept: "image/*" }}
                onChange={handleFileChange}
                sx={{ display: "none" }}
              />
            </Button>
            {selectedFiles.length > 0 && (
              <Grid container spacing={2} sx={{ mt: 2 }}>
                <Box
                  sx={{
                    display: "flex",
                    flexWrap: "nowrap",
                    overflowY: "hidden",
                    overflowX: "auto",
                    width: "100%",
                    alignItems: "center",
                    p: 2,
                  }}
                >
                  {selectedFiles.map((file, index) => (
                    <Box
                      key={index}
                      sx={{ flex: "0 0 auto", width: "200px", mr: 2 }}
                    >
                      <Box sx={{ position: "relative", height: "200px" }}>
                        <img
                          src={URL.createObjectURL(file)}
                          alt={`preview ${index}`}
                          style={{
                            width: "95%",
                            height: "100%",
                            objectFit: "cover",
                            borderRadius: "4px",
                          }}
                        />
                        <IconButton
                          color="secondary"
                          size="small"
                          sx={{
                            position: "absolute",
                            top: "8px",
                            right: "16px",
                            background: "rgba(0, 0, 0, 0.5)",
                            color: "white",
                          }}
                          onClick={() => handleFileRemove(file.name)}
                        >
                          <Delete />
                        </IconButton>
                      </Box>
                    </Box>
                  ))}
                </Box>
              </Grid>
            )}
          </Grid>
        </Grid>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={3}>
            <TextField
              fullWidth
              label="방 개수"
              type="number"
              name="roomCount"
              value={roomDetails.roomCount}
              onChange={handleRoomDetailChange}
              onInput={handleNumberInput}
              placeholder=""
            />
          </Grid>
          <Grid item xs={12} sm={3}>
            <TextField
              fullWidth
              label="욕실 개수"
              type="number"
              name="bathroomCount"
              value={roomDetails.bathroomCount}
              onChange={handleRoomDetailChange}
              onInput={handleNumberInput}
            />
          </Grid>
          <Grid item xs={12} sm={3}>
            <TextField
              select
              fullWidth
              label="난방 방식"
              name="heatingType"
              value={roomDetails.heatingType}
              onChange={handleRoomDetailChange}
            >
              <MenuItem value="개별">개별</MenuItem>
              <MenuItem value="중앙">중앙</MenuItem>
              <MenuItem value="지역">지역</MenuItem>
            </TextField>
          </Grid>
          <Grid item xs={12} sm={3}>
            <TextField
              select
              fullWidth
              label="방향"
              name="facing"
              value={roomDetails.facing}
              onChange={handleRoomDetailChange}
            >
              <MenuItem value="동">동</MenuItem>
              <MenuItem value="서">서</MenuItem>
              <MenuItem value="남">남</MenuItem>
              <MenuItem value="북">북</MenuItem>
            </TextField>
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              select
              fullWidth
              label="용도"
              name="purpose"
              value={roomDetails.purpose}
              onChange={handleRoomDetailChange}
            >
              <MenuItem value="단독주택">단독주택</MenuItem>
              <MenuItem value="공동주택">공동주택</MenuItem>
              <MenuItem value="업무시설">업무시설</MenuItem>
            </TextField>
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="총 주차 수"
              type="number"
              name="totalParking"
              value={roomDetails.totalParking}
              onChange={handleRoomDetailChange}
              onInput={handleNumberInput}
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="주차 가능 비율"
              name="parking"
              value={roomDetails.parking}
              onChange={handleRoomDetailChange}
              onInput={handleDecimalInput}
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="승인 날짜"
              type="date"
              name="approvalDate"
              value={roomDetails.approvalDate}
              onChange={handleRoomDetailChange}
              InputLabelProps={{ shrink: true }}
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="최초 등록 날짜"
              type="date"
              name="firstRegistrationDate"
              value={roomDetails.firstRegistrationDate}
              onChange={handleRoomDetailChange}
              InputLabelProps={{ shrink: true }}
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="입주 날짜"
              type="date"
              name="moveInDate"
              value={roomDetails.moveInDate}
              onChange={handleRoomDetailChange}
              InputLabelProps={{ shrink: true }}
            />
          </Grid>
        </Grid>
        <Box
          sx={{ border: "1px solid #ccc", borderRadius: 2, p: 2, mt: 3, mb: 2 }}
        >
          <Typography variant="h6" component="div" sx={{ mb: 2 }}>
            기본 옵션
          </Typography>
          <Grid container spacing={2} sx={{ pl: 2 }}>
            <FormControlLabel
              control={
                <Checkbox
                  checked={appliances.refrigerator}
                  onChange={handleApplianceChange}
                  name="refrigerator"
                />
              }
              label="냉장고"
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={appliances.washingMachine}
                  onChange={handleApplianceChange}
                  name="washingMachine"
                />
              }
              label="세탁기"
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={appliances.airConditioner}
                  onChange={handleApplianceChange}
                  name="airConditioner"
                />
              }
              label="에어컨"
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={appliances.bed}
                  onChange={handleApplianceChange}
                  name="bed"
                />
              }
              label="침대"
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={appliances.desk}
                  onChange={handleApplianceChange}
                  name="desk"
                />
              }
              label="책상"
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={appliances.microwave}
                  onChange={handleApplianceChange}
                  name="microwave"
                />
              }
              label="전자레인지"
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={appliances.closet}
                  onChange={handleApplianceChange}
                  name="closet"
                />
              }
              label="옷장"
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={appliances.chair}
                  onChange={handleApplianceChange}
                  name="chair"
                />
              }
              label="의자"
            />
          </Grid>
        </Box>
        <Box sx={{ textAlign: "end" }}>
          <Typography sx={{ color: "red", fontSize: theme.fontSize.small }}>
            {errMsg}
          </Typography>
        </Box>
      </Box>
      <Box
        sx={{
          mt: 4,
          mb: 12,
          display: "flex",
          justifyContent: "center",
          gap: 20,
        }}
      >
        <Button
          variant="contained"
          size="large"
          sx={{ bgcolor: theme.palette.common.grey }}
        >
          취소
        </Button>
        <Button
          variant="contained"
          size="large"
          color="primary"
          onClick={handleSubmit}
          disabled={isSubmitting}
        >
          등록
        </Button>
      </Box>

      <Modal open={open} onClose={handleClose}>
        <Box
          sx={{
            position: "absolute",
            top: "52%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: 320,
            height: 500,
            bgcolor: "background.paper",
            border: `2px solid ${theme.palette.common.grey}`,
            boxShadow: 24,
            p: 4,
          }}
        >
          <AddressSearch handleAddress={handleAddress} />
        </Box>
      </Modal>

      <Snackbar
        open={snackbarOpen}
        autoHideDuration={2000}
        onClose={handleSnackbarClose}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
        sx={{
          "& .MuiSnackbarContent-root": {
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            textAlign: "center",
            position: "fixed",
            bottom: "50%",
            transform: "translateY(50%)",
          },
        }}
      >
        <Alert
          onClose={handleSnackbarClose}
          severity="success"
          sx={{ width: "100%" }}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </Container>
  );
}
