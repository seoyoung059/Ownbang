import Axios from "./index";

// 북마크 조회 API 요청
export const Bookmarks = async () => {
  const response = await Axios.get(`/bookmarks`);
  return response.data;
};

// 북마크 등록 및 삭제 API 요청 (토글)
export const switchBookmarks = async (roomId) => {
  const response = await Axios.post(`/bookmarks/${roomId}`);
  return response.data;
};
