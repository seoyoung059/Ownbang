// 지도에서 입력받는 검색어
export const createSearchSlice = (set) => ({
  searchTerm: "",
  setSearchTerm: (term) => set({ searchTerm: term }),
});
