import { Bookmarks, addBookmarks } from "../api/bookmarks";

export const createBookmarks = (set) => ({
  bookmarkList: [],

  getBookmarks: async () => {
    try {
      const response = await Bookmarks();
      console.log("Fetched bookmarks:", response);
      set({
        bookmarkList: Array.isArray(response.data)
          ? response.data // 전체 bookmark 객체를 저장
          : [],
      });
    } catch (error) {
      console.error("Error fetching bookmarks:", error);
      set({ bookmarkList: [] });
    }
  },

  makeBookmarks: async (roomId) => {
    try {
      const response = await addBookmarks(roomId);
      console.log("Added bookmark:", response);
      return response;
    } catch (error) {
      console.error("Error making bookmarks:", error);
      throw error;
    }
  },
});
