export const createRealEstateSlice = (set) => ({
  value: 0,
  addValue: () => set((state) => ({ value: state.value++ })),
});
