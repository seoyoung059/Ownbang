import { getSearchList } from "../api/search";
import debounce from "lodash/debounce";

// 지도에서 입력받는 검색어
export const createSearchSlice = (set) => ({
  searchTerm: "",
  searchList: [],
  setSearchTerm: (term) => set({ searchTerm: term }),
  getSearchTerm: debounce(async (word) => {
    const result = await getSearchList(word);
    set({ searchList: result.data.searches });
  }, 600),
  setSearchList: (list) => set({ searchList: list }),
});
