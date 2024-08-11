import { Bookmarks, switchBookmarks } from "../api/bookmarks";

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

  toggleBookmarks: async (roomId) => {
    try {
      const response = await switchBookmarks(roomId);
      console.log("now bookmark:", response);
      return response;
    } catch (error) {
      console.error("Error making bookmarks:", error);
      throw error;
    }
  },
});
