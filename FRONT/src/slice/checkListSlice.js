export const createCheckListSlice = (set) => ({
  checklist: 2929,
  addValue: () => set((state) => ({ value: state.value++ })),
});
