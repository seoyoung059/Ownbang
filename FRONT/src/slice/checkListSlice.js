import {
  getChecklists,
  pathChecklists,
  addChecklist,
  deleteChecklist,
} from "../api/checklist";

export const createCheckListSlice = (set) => ({
  checklist: [],

  fetchCheckLists: async () => {
    const result = await getChecklists();
    set({ checklist: result.data.data });
    console.log(result.data.data);
  },

  modifyCheckLists: async (id, data) => {
    const result = await pathChecklists(id, data);
    console.log(result);
  },

  addNewTemplate: async (data) => {
    const result = await addChecklist(data);
    set((state) => ({
      checklist: [...state.checklist, result.data],
    }));
  },

  deleteTemplate: async (id) => {
    const result = await deleteChecklist(id);
    set((state) => ({
      checklist: state.checklist.filter((item) => item.checklistId !== id),
    }));
  },
});
