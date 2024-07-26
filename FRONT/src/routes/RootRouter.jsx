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

// 중개인일 경우 해당 페이지 이동, 아니면 메인 페이지 이동 - 추후 "중개인만 이용할 수 있습니다" 문구
const PrivateRoute = ({ element }) => {
  const user = useBoundStore((state) => state.user);

  useEffect(() => {
    if (user && !user.isAgent) {
      toast.info("중개인만 이용할 수 있습니다.", {
        position: "bottom-left",
        autoClose: 3000,
        hideProgressBar: true,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
      });
    }
  }, [user]);

  if (user && !user.isAgent) {
    return (
      <>
        <Navigate to="/" />
      </>
    );
  }

  return <>{element}</>;
};

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
        <MyPage />
      </Suspense>
    ),
  },
  {
    path: "/user-edit",
    element: (
      <Suspense fallback={<Loading message="내 정보를 불러오고 있어요." />}>
        <UserEditPage />
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
        <PrivateRoute element={<RealEstateRegisterPage />} />
      </Suspense>
    ),
  },
  {
    path: "/video-chat",
    element: (
      <Suspense fallback={<Loading />}>
        <VideoChat />
      </Suspense>
    ),
  },
  {
    path: "/agent",
    element: (
      <Suspense fallback={<Loading />}>
        <PrivateRoute element={<AgentPage />} />
      </Suspense>
    ),
  },
]);

export const RootRouter = () => {
  return <RouterProvider router={router} />;
};
