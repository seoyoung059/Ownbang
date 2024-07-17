import * as React from "react";
import { useState } from "react";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import Modal from "@mui/material/Modal";

const headerStyle = {
  position: "fixed",
  left: 0,
  top: 0,
  margin: "0 auto",
  width: "100%",
  height: "80px",
  backgroundColor: "#395886",
  color: "white",
};

const contentsStyle = {
  display: "flex",
  height: "100%",
  alignItems: "center",
  justifyContent: "space-between",
  margin: "0 40px",
};

const navigationStyle = {
  color: "white",
  "& div": {
    display: "flex",
    gap: "30px",
    "& button": {
      color: "white",
      backgroundColor: "#395886",
      border: "none",
    },
  },
};

const modalStyle = {
  position: "absolute",
  top: "10%",
  right: "5%",
  width: 400,
  bgcolor: "background.paper",
  borderRadius: "10px",
  boxShadow: 24,
  p: 4,
};

const Notification = ({ open, handleClose }) => {
  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={modalStyle}>
        <Typography id="modal-modal-title" variant="h6" component="h2">
          알림
        </Typography>
        <Typography id="modal-modal-description" sx={{ mt: 2 }}>
          noti -- data
        </Typography>
      </Box>
    </Modal>
  );
};

const Header = () => {
  const [open, setOpen] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  return (
    <>
      <Box sx={headerStyle}>
        <Box sx={contentsStyle}>
          <div>Logo</div>
          <Box sx={navigationStyle}>
            <div>
              <Button>로그인</Button>
              <Button onClick={handleOpen}>알림</Button>
              <Button>메뉴</Button>
            </div>
          </Box>
        </Box>
      </Box>
      <Notification open={open} handleClose={handleClose} />
    </>
  );
};

export default Header;
