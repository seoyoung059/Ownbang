import React, { createContext, useState, useEffect } from "react";
import { useCookies } from "react-cookie";
import { getAuthUser } from "../api/auth"; // 유저 정보를 가져오는 API 호출

export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [cookies] = useCookies(["token"]);

  useEffect(() => {
    const fetchUser = async () => {
      if (!cookies.token) {
        setLoading(false);
        return;
      }

      try {
        const userData = await getAuthUser(cookies.token);
        setUser(userData);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, [cookies.token]);

  return (
    <AuthContext.Provider value={{ user, loading }}>
      {children}
    </AuthContext.Provider>
  );
};
