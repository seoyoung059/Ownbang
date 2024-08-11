import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Input,
  Button,
  Box,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Paper,
  Grid,
  useMediaQuery,
} from "@mui/material";
import { useTheme } from "@mui/material";
import { useBoundStore } from "../../store/store";

const RealEstateSearchBar = ({ isMain }) => {
  const theme = useTheme();
  const navigate = useNavigate();
  const isSm = useMediaQuery(theme.breakpoints.down("sm"));
  const [inputValue, setInputValue] = useState("");
  const { getSearchTerm, setSearchTerm, searchList, setSearchList } =
    useBoundStore((state) => ({
      searchTerm: state.searchTerm,
      searchList: state.searchList,
      getSearchTerm: state.getSearchTerm,
      setSearchTerm: state.setSearchTerm,
      setSearchList: state.setSearchList,
    }));

  const onInputChange = (event) => {
    const value = event.target.value;
    setInputValue(value);
    if (value.trim() === "") {
      setSearchList([]);
    } else {
      getSearchTerm(value);
    }
  };

  const onSearchInputValue = () => {
    setSearchTerm(inputValue);
    navigate(`/estate?search=${inputValue}`, { replace: true });
    setInputValue("");
    setSearchList([]);
  };

  const onKeyDown = (event) => {
    if (event.key === "Enter") {
      onSearchInputValue();
    }
  };

  const handleSuggestionClick = (suggestion) => {
    setInputValue(suggestion.searchName);
    setSearchTerm(suggestion.searchName);
    navigate(`/estate?search=${suggestion.searchName}`, { replace: true });
    setSearchList([]);
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        width: "100%",
        position: "relative",
        alignItems: "center",
      }}
    >
      <Box
        sx={{
          width: "100%",
          maxWidth: isMain ? "80%" : "auto",
          position: "relative",
        }}
      >
        <Grid container spacing={1}>
          <Grid item xs={11}>
            <Input
              type="text"
              placeholder={
                isMain
                  ? "원하시는 원룸의 지역명, 지하철역, 단지명(아파트명)을 입력해주세요"
                  : "지역명 + 역명으로 검색하세요"
              }
              value={inputValue}
              onChange={onInputChange}
              onKeyDown={onKeyDown}
              disableUnderline
              sx={{
                width: "100%",
                borderRadius: "7px",
                border: "1px solid lightgray",
                padding: "15px 25px",
                backgroundColor: theme.palette.common.white,
                fontSize: theme.fontSize.medium,
                boxShadow: 3,
              }}
            />
          </Grid>
          <Grid item xs={1}>
            <Button
              variant="contained"
              onClick={onSearchInputValue}
              sx={{
                borderRadius: "7px",
                padding: "13px 15px",
                backgroundColor: theme.palette.primary.dark,
                fontSize: theme.fontSize.medium,
                height: "100%",
                width: "100%",
              }}
            >
              검색
            </Button>
          </Grid>
        </Grid>
        {searchList.length > 0 && (
          <Paper
            sx={{
              position: "absolute",
              top: "100%",
              width: isSm ? "88.8%" : "90.8%",
              left: 0,
              zIndex: 1,
              border: "1px solid lightgray",
              borderRadius: "7px",
              boxShadow: 3,
              mt: 1,
              bgcolor: theme.palette.common.white,
              pl: 1,
            }}
          >
            <List>
              {searchList.slice(0, 6).map((suggestion, index) => (
                <ListItem key={index} disablePadding>
                  <ListItemButton
                    onClick={() => handleSuggestionClick(suggestion)}
                  >
                    <ListItemText primary={suggestion.searchName} />
                  </ListItemButton>
                </ListItem>
              ))}
            </List>
          </Paper>
        )}
      </Box>
    </Box>
  );
};

export default RealEstateSearchBar;
