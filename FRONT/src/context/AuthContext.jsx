import React, { createContext, useState, useEffect } from "react";
import { useCookies } from "react-cookie";

// 로그인 상태는 많이 바뀌는 게 아니라서 zustand 대신 useContext를 사용하기 위해 이 부분만 따로 만듬
// react-cookie에 토큰 저장하고 토큰이 있냐 없냐에 따라 로그인 검증

export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);
  const [cookies, setCookie, removeCookie] = useCookies(["token"]);

  useEffect(() => {
    const token = cookies.token;
    if (token) {
      setIsAuthenticated(true);
    } else {
      setIsAuthenticated(false);
    }
    setLoading(false);
  }, [cookies.token]);

  const login = (token) => {
    setCookie("token", token, { path: "/" });
    setIsAuthenticated(true);
  };

  const logout = () => {
    removeCookie("token", { path: "/" });
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
