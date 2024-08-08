import React from "react";
import Header from "./components/common/Header";
import { CookiesProvider } from "react-cookie";
import { RootRouter } from "./routes/RootRouter";
import { ToastContainer } from "react-toastify";

function App() {
  return (
    <>
      <CookiesProvider>
        <RootRouter />
        <ToastContainer />
      </CookiesProvider>
    </>
  );
}

export default App;
