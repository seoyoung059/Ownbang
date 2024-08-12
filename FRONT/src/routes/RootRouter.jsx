import React, { lazy, Suspense } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Header from "../components/common/Header";
import Loading from "../components/common/Loading";
import { AgentRoute, UserRoute } from "./CustomRoute";

const MainPage = lazy(() => import("../pages/MainPage"));
const LoginPage = lazy(() => import("../pages/LoginPage"));
const SignUpPage = lazy(() => import("../pages/SignUpPage"));
const MyPage = lazy(() => import("../pages/MyPage"));
const UserEditPage = lazy(() => import("../pages/UserEditPage"));
const RealEstatePage = lazy(() => import("../pages/RealEstatePage"));
const RealEstateRegisterPage = lazy(() =>
  import("../pages/RealEstateRegisterPage")
);
const VideoChat = lazy(() => import("../pages/VideoChatPage"));
const AgentPage = lazy(() => import("../pages/AgentPage"));
const ReplayPage = lazy(() => import("../pages/ReplayPage"));

const RootRouter = () => {
  return (
    <Router>
      <Header />
      <Suspense fallback={<Loading />}>
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignUpPage />} />
          <Route path="/mypage" element={<UserRoute element={<MyPage />} />} />
          <Route
            path="/user-edit"
            element={
              <UserRoute element={<UserEditPage />} allowAgents={true} />
            }
          />
          <Route path="/estate" element={<RealEstatePage />} />
          <Route
            path="/estate-register"
            element={<AgentRoute element={<RealEstateRegisterPage />} />}
          />
          <Route
            path="/video-chat/:reservationId"
            element={<UserRoute element={<VideoChat />} allowAgents={true} />}
          />
          <Route
            path="/agent"
            element={<AgentRoute element={<AgentPage />} />}
          />
          <Route
            path="/replay/:reservationId"
            element={<UserRoute element={<ReplayPage />} />}
          />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Suspense>
    </Router>
  );
};

export { RootRouter };
