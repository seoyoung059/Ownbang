import { Box, Button, Input } from "@mui/material";
import { useTheme } from "@mui/material";
import { useState, useRef } from "react";

const CheckListAddInput = ({ onCreate }) => {
  const theme = useTheme();
  const inputRef = useRef();

  const [inputValue, setInputValue] = useState("");

  const onChange = (e) => {
    setInputValue(e.target.value);
  };

  const onKeyDown = (e) => {
    if (e.keyCode === 13) {
      onSubmit();
    }
  };

  const onSubmit = () => {
    if (inputValue === "") {
      inputRef.current.focus();
      return;
    }
    onCreate(inputValue);
    setInputValue("");
  };

  return (
    <Box sx={{ display: "flex", gap: "10px", justifyContent: "center" }}>
      <Input
        ref={inputRef}
        value={inputValue}
        onChange={onChange}
        onKeyDown={onKeyDown}
        placeholder="새로운 항목을 입력하세요"
        sx={{
          fontSize: theme.fontSize.small,
          width: "350px",
          padding: "10px",
          border: "1px solid lightgrey",
          borderRadius: "10px",
          "&:before": {
            borderBottom: "none",
          },
          "&:after": {
            borderBottom: "none",
          },
          "&:hover:not(.Mui-disabled):before": {
            borderBottom: "none",
          },
          "&:hover:not(.Mui-disabled):after": {
            borderBottom: "none",
          },
          "&.Mui-focused:before": {
            borderBottom: "none",
          },
          "&.Mui-focused:after": {
            borderBottom: "none",
          },
        }}
      />

      <Button
        onClick={onSubmit}
        sx={{
          backgroundColor: theme.palette.primary.main,
          color: theme.palette.common.white,
          cursor: "pointer",
          border: "none",
          borderRadius: "5px",
          fontSize: theme.fontSize.small,
        }}
      >
        추가
      </Button>
    </Box>
  );
};

export default CheckListAddInput;
