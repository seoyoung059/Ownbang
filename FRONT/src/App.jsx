import React from "react";
import Header from "./components/common/Header";
import { CookiesProvider } from "react-cookie";
import { AuthProvider } from "./context/AuthContext";
import { RootRouter } from "./routes/RootRouter";
import { ToastContainer } from "react-toastify";

function App() {
  return (
    <>
      <CookiesProvider>
        <AuthProvider>
          <Header />
          <RootRouter />
          <ToastContainer />
        </AuthProvider>
      </CookiesProvider>
    </>
  );
}

export default App;
