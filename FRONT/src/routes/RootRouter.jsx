import {
  createBrowserRouter,
  RouterProvider,
  Navigate,
} from "react-router-dom";
import { lazy, Suspense, useEffect } from "react";

import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import Loading from "../components/common/Loading";

import { useBoundStore } from "../store/store";

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

// 추후 다른 로딩에도 메세지 전달하고 로딩 페이지도 디자인 수정
const router = createBrowserRouter([
  {
    path: "/",
    element: (
      <Suspense fallback={<Loading />}>
        <MainPage />
      </Suspense>
    ),
  },
  {
    path: "/login",
    element: (
      <Suspense fallback={<Loading />}>
        <LoginPage />
      </Suspense>
    ),
  },
  {
    path: "/signup",
    element: (
      <Suspense fallback={<Loading />}>
        <SignUpPage />
      </Suspense>
    ),
  },
  {
    path: "/mypage",
    element: (
      <Suspense fallback={<Loading />}>
        <UserRoute element={<MyPage />} />
      </Suspense>
    ),
  },
  {
    path: "/user-edit",
    element: (
      <Suspense fallback={<Loading message="내 정보를 불러오고 있어요." />}>
        <UserRoute element={<UserEditPage />} />
      </Suspense>
    ),
  },
  {
    path: "/estate",
    element: (
      <Suspense fallback={<Loading message="매물을 검색하고 있어요." />}>
        <RealEstatePage />
      </Suspense>
    ),
  },
  {
    path: "/estate-register",
    element: (
      <Suspense fallback={<Loading />}>
        <AgentRoute element={<RealEstateRegisterPage />} />
      </Suspense>
    ),
  },
  {
    path: "/video-chat",
    element: (
      <Suspense fallback={<Loading />}>
        <UserRoute element={<VideoChat />} />
      </Suspense>
    ),
  },
  {
    path: "/agent",
    element: (
      <Suspense fallback={<Loading />}>
        <AgentRoute element={<AgentPage />} />
      </Suspense>
    ),
  },
]);

export const RootRouter = () => {
  return <RouterProvider router={router} />;
};
