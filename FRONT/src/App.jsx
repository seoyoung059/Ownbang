import React from "react";

import Header from "./components/common/Header";

import { RootRouter } from "./routes/RootRouter";

function App() {
  return (
    <>
      <Header />
      <RootRouter />
    </>
  );
}

export default App;
