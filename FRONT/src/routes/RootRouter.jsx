import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { lazy, Suspense } from "react";
import Loading from "../components/common/Loading";

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
        <RealEstateRegisterPage />
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
]);

export const RootRouter = () => {
  return <RouterProvider router={router} />;
};
