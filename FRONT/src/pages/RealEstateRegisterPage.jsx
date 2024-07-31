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
} from "@mui/material";
import { Delete } from "@mui/icons-material";
import { useTheme } from "@mui/material";
import AddressSearch from "../components/common/AddressSearch";
import RealEstateMap from "../components/real-estate/RealEstateMap";

export default function RealEstateRegisterPage() {
  const theme = useTheme();
  const [address, setAddress] = useState("");
  const [coordinates, setCoordinates] = useState({ lat: null, lng: null });
  const [open, setOpen] = useState(false);
  const [selectedFiles, setSelectedFiles] = useState([]);
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
    room_count: "",
    bathroom_count: "",
    heating_type: "개별",
    movein_date: "",
    building_floor: "",
    elevator: false,
    total_parking: "",
    parking: "",
    approval_date: "",
    first_registration_date: "",
    facing: "동",
    purpose: "단독주택",
    road: "",
    detail_address: "",
  });

  const handleAddress = (newAddress) => {
    setAddress(newAddress);
    handleClose();
  };

  const handleFileChange = (event) => {
    const files = Array.from(event.target.files);
    const newFiles = files.filter(
      (file) =>
        !selectedFiles.some((selectedFile) => selectedFile.name === file.name)
    );
    setSelectedFiles([...selectedFiles, ...newFiles]);
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
        console.log(coords);
      }
    });
  }, [address]);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

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
              defaultValue="오피스텔"
            >
              <MenuItem value="오피스텔">오피스텔</MenuItem>
              <MenuItem value="아파트">아파트</MenuItem>
              <MenuItem value="빌라">빌라</MenuItem>
            </TextField>
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField select fullWidth label="거래 종류" defaultValue="월세">
              <MenuItem value="월세">월세</MenuItem>
              <MenuItem value="전세">전세</MenuItem>
              <MenuItem value="매매">매매</MenuItem>
            </TextField>
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField select fullWidth label="구조" defaultValue="원룸">
              <MenuItem value="원룸">원룸</MenuItem>
              <MenuItem value="투룸">투룸</MenuItem>
              <MenuItem value="쓰리룸">쓰리룸</MenuItem>
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
                  <RealEstateMap
                    searchTerm={address}
                    mark={coordinates}
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
                      placeholder="추가 주소 정보를 입력하세요. (동, 호수 등)"
                    />
                  </Grid>
                  <Grid item xs={8}>
                    <TextField fullWidth label="해당 층" />
                  </Grid>
                  <Grid
                    item
                    xs={4}
                    sx={{ display: "flex", justifyContent: "center" }}
                  >
                    <FormControlLabel
                      control={<Checkbox defaultChecked />}
                      label="복층여부"
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField fullWidth label="전용 면적" placeholder="㎡" />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField fullWidth label="공급 면적" placeholder="㎡" />
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
                  placeholder="추가 주소 정보를 입력하세요. (동, 호수 등)"
                />
              </Grid>
              <Grid item xs={12} sm={8}>
                <TextField fullWidth label="해당 층" />
              </Grid>
              <Grid
                item
                xs={12}
                sm={4}
                sx={{ display: "flex", justifyContent: "center" }}
              >
                <FormControlLabel
                  control={<Checkbox defaultChecked />}
                  label="복층여부"
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField fullWidth label="전용 면적" placeholder="㎡" />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField fullWidth label="공급 면적" placeholder="㎡" />
              </Grid>
            </>
          )}
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="보증금"
              defaultValue="3000"
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
              defaultValue="50"
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
              defaultValue="8"
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
                    overflowY: "hidden", // 가로 스크롤바 제거
                    overflowX: "auto", // 세로 스크롤바 활성화
                    width: "100%",
                    alignItems: "center", // 버튼과 이미지 리스트를 수평으로 맞추기
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
                            color: "white", // 아이콘 색상을 흰색으로 설정
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
              name="room_count"
              value={roomDetails.room_count}
              onChange={handleRoomDetailChange}
            />
          </Grid>
          <Grid item xs={12} sm={3}>
            <TextField
              fullWidth
              label="욕실 개수"
              type="number"
              name="bathroom_count"
              value={roomDetails.bathroom_count}
              onChange={handleRoomDetailChange}
            />
          </Grid>
          <Grid item xs={12} sm={3}>
            <TextField
              select
              fullWidth
              label="난방 방식"
              name="heating_type"
              value={roomDetails.heating_type}
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
              name="total_parking"
              value={roomDetails.total_parking}
              onChange={handleRoomDetailChange}
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="주차 가능 비율"
              type="number"
              name="parking"
              value={roomDetails.parking}
              onChange={handleRoomDetailChange}
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="승인 날짜"
              type="date"
              name="approval_date"
              value={roomDetails.approval_date}
              onChange={handleRoomDetailChange}
              InputLabelProps={{ shrink: true }}
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="최초 등록 날짜"
              type="date"
              name="first_registration_date"
              value={roomDetails.first_registration_date}
              onChange={handleRoomDetailChange}
              InputLabelProps={{ shrink: true }}
            />
          </Grid>
          <Grid item xs={12} sm={4}>
            <TextField
              fullWidth
              label="입주 날짜"
              type="date"
              name="movein_date"
              value={roomDetails.movein_date}
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
        <Button variant="contained" size="large" color="primary">
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
    </Container>
  );
}