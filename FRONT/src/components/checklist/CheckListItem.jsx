import { Box, Typography, Button, Checkbox, IconButton } from "@mui/material";
import { useTheme } from "@mui/material";
import DeleteOutlineRoundedIcon from "@mui/icons-material/DeleteOutlineRounded";
import AccessTimeIcon from "@mui/icons-material/AccessTime";
import MoreTimeIcon from "@mui/icons-material/MoreTime";

const CheckListItem = ({
  id,
  isDone,
  content,
  timestamp, // 타임스탬프 prop 추가
  onUpdate,
  onDelete,
  onTimestampClick,
  canEdit,
  forReplay,
}) => {
  const theme = useTheme();

  const onToggleCheck = () => {
    onUpdate(id);
  };
  const onClickDeleteButton = () => {
    onDelete(id);
  };

  const onClickTimestamp = () => {
    if (onTimestampClick && timestamp) {
      onTimestampClick(timestamp); // 타임스탬프 클릭 시 핸들러 호출
    }
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
        {
        /* canEdit(chat, replay)에서 replay가 아니면 => chat*/
        canEdit && !forReplay ? (
          <IconButton
            onClick={onToggleCheck}
            sx={{ width: "20px", height: "20px" }}
          >
            {/* 체크 여부에 따라 */}
            {isDone ? (
              <AccessTimeIcon sx={{ color: theme.palette.success.main }} />
            ) : (
              <MoreTimeIcon sx={{ color: theme.palette.grey[500] }} />
            )}
          </IconButton>
        ) : (
          <Box sx={{ width: "20px", height: "20px" }} />
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
        {
        /* chat, replay에서 isDone(체크됨)이면 타임스탬프 표시 */
        canEdit &&
          isDone && ( // isDone이 true일 때만 타임스탬프 표시
            <Button
              onClick={onClickTimestamp}
              variant="text"
              sx={{
                fontSize: "10px",
                color: "gray",
              }}
            >
              {timestamp}
            </Button>
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
