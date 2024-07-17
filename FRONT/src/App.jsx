import React from "react";

import Button from "@mui/material/Button";

import { useTheme } from "@mui/material";

function App() {
  const theme = useTheme();
  return (
    <>
      <Button
        variant="contained"
        sx={{
          backgroundColor: theme.palette.primary.main,
          fontSize: theme.fontSize.basic,
        }}
      >
        Hello world
      </Button>
      <span
        style={{
          fontWeight: theme.typography.fontWeight,
          fontSize: theme.typography.fontSize,
        }}
      >
        하이
      </span>
    </>
  );
}

export default App;
