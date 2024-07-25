import React, { createContext, useState, useEffect } from "react";
import { useCookies } from "react-cookie";

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
