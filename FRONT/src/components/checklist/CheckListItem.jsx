import { Box, Typography, Button, Checkbox, IconButton } from "@mui/material";
import { useTheme } from "@mui/material";
import DeleteOutlineRoundedIcon from "@mui/icons-material/DeleteOutlineRounded";

const CheckListItem = ({
  id,
  isDone,
  content,
  timestamp, // 타임스탬프 prop 추가
  onUpdate,
  onDelete,
  canEdit,
}) => {
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
        paddingY: "8px",
      }}
    >
      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          gap: "10px",
        }}
      >
        {canEdit ? (
          <Checkbox
            onChange={onChangeCheckBox}
            sx={{ width: "20px" }}
            checked={isDone}
          />
        ) : (
          <Box sx={{ width: "20px" }} />
        )}

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
        {canEdit &&
          isDone && ( // isDone이 true일 때만 타임스탬프 표시
            <Typography
              sx={{
                fontSize: "10px",
                color: "gray",
              }}
            >
              {timestamp}
            </Typography>
          )}
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
