import React, { useEffect, useState } from "react";
import {
  Container,
  CssBaseline,
  Box,
  Typography,
  Divider,
  Grid,
  TextField,
  Button,
  Avatar,
  Snackbar,
  Alert,
} from "@mui/material";
import { useTheme } from "@mui/material";
import StatusChangeForm from "./StatusChangeForm";
import PasswordChangeForm from "./PasswordChangeForm";
import { useNavigate } from "react-router-dom";
import { useBoundStore } from "../../store/store";

export default function UserInfoEditForm() {
  const { user, fetchUser, modifyUser } = useBoundStore((state) => ({
    user: state.user,
    fetchUser: state.fetchUser,
    modifyUser: state.modifyUser,
  }));

  useEffect(() => {
    fetchUser();
  }, [fetchUser]);

  const navigate = useNavigate();
  const theme = useTheme();

  const [userInfo, setUserInfo] = useState(user);
  const [profileImage, setProfileImage] = useState(user.profileImageUrl);
  const [selectedFile, setSelectedFile] = useState(null);

  const [forAgent, setForAgent] = useState(false);
  const [forPassChange, setForPassChange] = useState(false);

  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [snackbarMessage, setSnackbarMessage] = useState("");

  const handleSnackbarClose = () => {
    setSnackbarOpen(false);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserInfo((prevUserInfo) => ({
      ...prevUserInfo,
      [name]: value,
    }));
  };

  const handleEdit = (props) => {
    setForAgent(props);
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    const validImageTypes = ["image/jpeg", "image/png", "image/jpg"];

    if (file && validImageTypes.includes(file.type)) {
      setSelectedFile(file);
      setProfileImage(URL.createObjectURL(file));
    } else {
      setSnackbarMessage("올바른 이미지 파일을 선택하세요 (jpeg, jpg, png).");
      setSnackbarOpen(true);
    }
  };

  const handlePassChange = (props) => {
    setForPassChange(props);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const data = { nickname: userInfo.nickname };

    const formData = new FormData();
    if (selectedFile) {
      formData.append("file", selectedFile);
    }
    formData.append(
      "data",
      new Blob([JSON.stringify(data)], {
        type: "application/json",
      })
    );

    const res = await modifyUser(formData);
    if (res.resultCode === "SUCCESS") {
      await fetchUser();
      setSnackbarMessage("수정이 완료되었습니다!");
      setSnackbarOpen(true);
      setTimeout(() => {
        navigate("/");
      }, 1000);
    } else {
      setSnackbarMessage("수정에 실패했습니다. 다시 시도해주세요.");
      setSnackbarOpen(true);
    }
  };

  const handleCancel = () => {
    navigate("/");
  };

  return (
    <>
      <Container component="main" maxWidth="xs">
        <CssBaseline />

        {!forAgent && !forPassChange ? (
          <Box
            sx={{
              mt: 8,
              display: "flex",
              flexDirection: "column",
            }}
          >
            <Typography
              sx={{ mt: 12, pl: 2, fontWeight: theme.fontWeight.bold }}
              component="h1"
              variant="h5"
            >
              개인정보 변경
            </Typography>
            <Typography
              variant="body2"
              sx={{
                textAlign: "end",
                fontSize: theme.fontSize.small,
                color: theme.palette.primary.main,
                "&:hover": {
                  textDecoration: "underLine",
                },
              }}
              onClick={() => setForAgent((prev) => !prev)}
            >
              {user.isAgent ? "중개인 정보 수정" : "중개인 회원 전환 신청"}
            </Typography>
            <Divider component="div" sx={{ mt: 1, width: "100%" }} />
            <Box component="form" noValidate sx={{ mt: 4 }}>
              <Grid container spacing={4}>
                <Grid item xs={12} sm={6}>
                  <Avatar
                    src={profileImage} // 수정된 부분
                    alt="프로필 이미지"
                    sx={{ width: 260, height: 260 }}
                  />
                </Grid>
                <Grid
                  item
                  xs={12}
                  sm={6}
                  sx={{
                    display: "flex",
                    justifyContent: "end",
                    alignItems: "end",
                  }}
                >
                  <Button
                    variant="contained"
                    component="label"
                    sx={{ height: "20%", width: "60%" }}
                  >
                    이미지 찾기
                    <input
                      type="file"
                      hidden
                      accept="image/*"
                      onChange={handleFileChange}
                    />
                  </Button>
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    label="닉네임"
                    id="nickname"
                    name="nickname"
                    value={userInfo.nickname}
                    onChange={handleInputChange}
                    fullWidth
                  />
                </Grid>
              </Grid>
              <Grid>
                <Typography
                  variant="body2"
                  sx={{
                    mt: 2,
                    textAlign: "end",
                    fontSize: theme.fontSize.small,
                    color: theme.palette.primary.main,
                    "&:hover": {
                      textDecoration: "underLine",
                    },
                  }}
                  onClick={() => setForPassChange((prev) => !prev)}
                >
                  비밀번호 변경
                </Typography>
              </Grid>
              <Grid
                item
                xs={8}
                sx={{ display: "flex", justifyContent: "space-around" }}
              >
                <Button
                  variant="contained"
                  onClick={handleCancel}
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
        ) : forPassChange ? (
          <PasswordChangeForm toggleEdit={handlePassChange} />
        ) : (
          <StatusChangeForm toggleEdit={handleEdit} isAgent={user.isAgent} />
        )}
        <Snackbar
          open={snackbarOpen}
          autoHideDuration={2000}
          onClose={handleSnackbarClose}
          anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
          sx={{
            mt: 2,
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
            severity={snackbarMessage.includes("완료") ? "success" : "error"}
            sx={{ width: "100%" }}
          >
            {snackbarMessage}
          </Alert>
        </Snackbar>
      </Container>
    </>
  );
}
