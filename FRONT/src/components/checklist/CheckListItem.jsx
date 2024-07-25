// 항목 하나하나를 구성하는 컴포넌트
// 체크박스, 콘텐츠, 삭제버튼

import { Box, Typography, Button, Checkbox, IconButton } from "@mui/material";
import { useTheme } from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import DeleteOutlineRoundedIcon from "@mui/icons-material/DeleteOutlineRounded";

const CheckListItem = ({ id, isDone, content, date, onUpdate, onDelete }) => {
  const theme = useTheme();

  const onChangeCheckBox = () => {
    onUpdate(id);
  };

  const onClickDeleteButton = () => {
    onDelete(id);
  };

  return (
    <Box
      className="list"
      sx={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        borderBottom: "1px solid lightgrey",
      }}
    >
      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          gap: "10px",
        }}
      >
        <Checkbox
          onChange={onChangeCheckBox}
          sx={{ width: "20px" }}
          defaultChecked={isDone}
        />
        <Typography
          sx={{
            fontSize: "12px",
          }}
        >
          {content}
        </Typography>
      </Box>

      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          gap: "10px",
        }}
      >
        {/* <Typography
          sx={{
            fontSize: "12px",
          }}
        >
          타임스탬프
        </Typography> */}
        <IconButton
          onClick={onClickDeleteButton}
          sx={{
            fontSize: theme.fontSize.small,
            backgroundColor: "#f0f3fa",
            borderRadius: "50px",
            padding: "3px",
          }}
        >
          <DeleteOutlineRoundedIcon />
        </IconButton>
      </Box>
    </Box>
  );
};

export default CheckListItem;
