import React, { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useBoundStore } from "../store/store";

export const AgentRoute = ({ element }) => {
  const { isAuthenticated, user, fetchUser } = useBoundStore((state) => ({
    isAuthenticated: state.isAuthenticated,
    user: state.user,
    fetchUser: state.fetchUser,
  }));

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      await fetchUser();
      setLoading(false);
    };

    fetchData();
  }, [fetchUser]);

  useEffect(() => {
    if (!loading) {
      if (!isAuthenticated) {
        toast.info("로그인이 필요합니다.", {
          position: "bottom-left",
          autoClose: 2000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "light",
        });
      } else if (user && !user.isAgent) {
        toast.info("중개인만 이용할 수 있습니다.", {
          position: "bottom-left",
          autoClose: 2000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "light",
        });
      }
    }
  }, [isAuthenticated, user, loading]);

  if (loading) {
    return null; // Or a loading spinner
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  if (isAuthenticated && user && !user.isAgent) {
    return <Navigate to="/" />;
  }

  return <>{element}</>;
};

export const UserRoute = ({ element, allowAgents = false }) => {
  const { isAuthenticated, user, fetchUser } = useBoundStore((state) => ({
    isAuthenticated: state.isAuthenticated,
    user: state.user,
    fetchUser: state.fetchUser,
  }));

  useEffect(() => {
    const fetchData = async () => {
      await fetchUser();
      setLoading(false);
    };

    fetchData();
  }, [fetchUser]);

  useEffect(() => {
    if (!isAuthenticated) {
      toast.info("로그인이 필요합니다.", {
        position: "bottom-left",
        autoClose: 2000,
        hideProgressBar: true,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
      });
    }
  }, [isAuthenticated]);

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  if (isAuthenticated && user && user.isAgent && !allowAgents) {
    return <Navigate to="/agent" />;
  }

  return <>{element}</>;
};
