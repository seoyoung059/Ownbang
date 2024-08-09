import React, { useState } from "react";
import {
  Stack,
  Autocomplete,
  TextField,
  Box,
  Button,
  IconButton,
  InputBase,
  Snackbar,
  Alert,
} from "@mui/material";
import { useTheme } from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";

const CheckListTitle = ({
  checklist,
  setSelectedTitle,
  addTemplate,
  deleteTemplate,
  modifyTemplateTitle,
}) => {
  const theme = useTheme();
  const [newTemplateTitle, setNewTemplateTitle] = useState("");
  const [editTitle, setEditTitle] = useState(null);
  const [editValue, setEditValue] = useState("");
  const [errorMessage, setErrorMessage] = useState(null);

  const options = checklist
    ? checklist.map((el) => ({
        checklistId: el.checklistId,
        title: el.title,
      }))
    : [];

  const handleTemplateChange = (event, value) => {
    if (value && typeof value === "string") {
      setNewTemplateTitle(value);
    } else if (value && value.title) {
      setSelectedTitle(value.title);
      setNewTemplateTitle("");
    } else {
      setSelectedTitle("");
    }
  };

  const handleAddTemplate = async () => {
    if (newTemplateTitle.trim() !== "") {
      try {
        const result = await addTemplate(newTemplateTitle);
        setSelectedTitle(newTemplateTitle);
        setNewTemplateTitle(newTemplateTitle);
      } catch (error) {
        if (error.response && error.response.status === 409) {
          setErrorMessage("이미 존재하는 체크리스트 템플릿입니다.");
        } else {
          setErrorMessage("템플릿을 추가하는 중 오류가 발생했습니다.");
        }
      }
    }
  };
  const handleDeleteTemplate = async (event, checklistId) => {
    event.stopPropagation();
    await deleteTemplate(checklistId);
    setSelectedTitle("");
  };

  const handleTitleDoubleClick = (option) => {
    setEditTitle(option.checklistId);
    setEditValue(option.title);
  };

  const handleTitleChange = (e) => {
    setEditValue(e.target.value);
  };

  const handleTitleBlur = async (option) => {
    await modifyTemplateTitle(option.checklistId, editValue);
    setEditTitle(null);
    setSelectedTitle(editValue);
  };

  const handleCloseSnackbar = () => {
    setErrorMessage(null);
  };

  return (
    <Stack spacing={2} sx={{ width: "100%" }}>
      <Box sx={{ display: "flex", alignItems: "end" }}>
        <Autocomplete
          freeSolo
          id="templateChoice"
          options={options}
          getOptionLabel={(option) => option.title}
          onInputChange={(event, value) => {
            setNewTemplateTitle(value);
            if (!value) setSelectedTitle("");
          }}
          onChange={handleTemplateChange}
          inputValue={newTemplateTitle}
          renderInput={(params) => (
            <TextField
              {...params}
              variant="standard"
              label="탬플릿"
              placeholder="사용할 템플릿을 선택하거나 추가하세요"
              InputProps={{
                ...params.InputProps,
                sx: { padding: "10px", fontSize: "12px" },
              }}
              sx={{
                "& .MuiInputBase-root": { padding: "10px", fontSize: "12px" },
              }}
            />
          )}
          renderOption={(props, option) => (
            <li
              {...props}
              key={option.checklistId}
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
              }}
              onDoubleClick={() => handleTitleDoubleClick(option)}
            >
              {editTitle === option.checklistId ? (
                <InputBase
                  value={editValue}
                  onChange={handleTitleChange}
                  onBlur={() => handleTitleBlur(option)}
                  autoFocus
                  sx={{ fontSize: "12px" }}
                />
              ) : (
                option.title
              )}
              <IconButton
                edge="end"
                aria-label="delete"
                onClick={(event) =>
                  handleDeleteTemplate(event, option.checklistId)
                }
              >
                <DeleteIcon />
              </IconButton>
            </li>
          )}
          sx={{ flexGrow: 1 }}
        />
        <Button onClick={handleAddTemplate} variant="contained" sx={{ ml: 1 }}>
          추가
        </Button>
      </Box>
      {errorMessage && (
        <Snackbar
          open={Boolean(errorMessage)}
          autoHideDuration={6000}
          onClose={handleCloseSnackbar}
        >
          <Alert
            onClose={handleCloseSnackbar}
            severity="error"
            sx={{ width: "100%" }}
          >
            {errorMessage}
          </Alert>
        </Snackbar>
      )}
    </Stack>
  );
};

export default CheckListTitle;
