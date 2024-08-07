import Axios from "./index";

// 북마크 등록 API 요청
export const addBookmarks = async (roomId, favorite) => {
  const response = await Axios.post(`/bookmarks/${roomId}`, roomId);
  return response.data;
};

// 북마크 조회 API 요청
export const Bookmarks = async () => {
  const response = await Axios.get(`/bookmarks`);
  return response.data;
};
