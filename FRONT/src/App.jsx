import React from "react";
import Header from "./components/common/Header";
import { CookiesProvider } from "react-cookie";
import { AuthProvider } from "./context/AuthContext";
import { RootRouter } from "./routes/RootRouter";

function App() {
  return (
    <>
      <CookiesProvider>
        <AuthProvider>
          <Header />
          <RootRouter />
        </AuthProvider>
      </CookiesProvider>
    </>
  );
}

export default App;
