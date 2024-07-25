import * as React from "react";
import { Stack, Autocomplete, TextField } from "@mui/material";
import { useTheme } from "@mui/material";

const CheckListTitle = ({ setSelectedTitle }) => {
  const theme = useTheme();
  const templateTitles = [{ title: "원룸 리스트" }];

  const handleChange = (event, value) => {
    setSelectedTitle(value ? value.title : null);
  };

  return (
    <Stack spacing={2} sx={{ width: 300 }}>
      <Autocomplete
        id="templateChoice"
        options={templateTitles}
        getOptionLabel={(option) => option.title}
        onChange={handleChange}
        sx={{ padding: "10px" }}
        renderInput={(params) => (
          <TextField
            {...params}
            variant="standard"
            label="Template"
            placeholder="사용할 템플릿을 선택해주세요"
            InputProps={{
              ...params.InputProps,
              sx: {
                padding: "10px",
                fontSize: "12px",
              },
            }}
            sx={{
              "& .MuiInputBase-root": {
                padding: "10px",
                fontSize: "12px",
              },
            }}
          />
        )}
        renderOption={(props, option) => (
          <li {...props} style={{ fontSize: "12px" }}>
            {option.title}
          </li>
        )}
      />
    </Stack>
  );
};

export default CheckListTitle;
