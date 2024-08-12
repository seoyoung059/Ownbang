import { getSearchList } from "../api/search";

// 지도에서 입력받는 검색어
export const createSearchSlice = (set) => ({
  searchTerm: "",
  searchList: [],
  setSearchTerm: (term) => set({ searchTerm: term }),
  getSearchTerm: async (word) => {
    const result = await getSearchList(word);
    set({ searchList: result.data.searches });
  },
  setSearchList: (list) => set({ searchList: list }),
});
