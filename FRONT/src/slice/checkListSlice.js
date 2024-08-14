import {
  getChecklists,
  getChecklist,
  patchChecklists,
  addChecklist,
  deleteChecklist,
  addChecklist_video,
} from "../api/checklist";

export const createCheckListSlice = (set) => ({
  checklist: [],

  fetchCheckLists: async (id) => {
    // const result = await getChecklists();
    // set({ checklist: result.data.data });
    // console.log(result.data.data);

    // id에 맞는 체크리스트 하나만 가져와서 목록에 넣음
    const result = await getChecklist(id);
    set({checklist: [result.data]});
    console.log(result.data);
  },

  modifyCheckLists: async (id, data) => {
    const result = await patchChecklists(id, data);
    console.log(result);
  },

  addNewTemplate: async (data) => {
    const result = await addChecklist(data);
    set((state) => ({
      checklist: [...state.checklist, result.data],
    }));
  },

  stampCheckList: async (id, data) => {
    const result = await addChecklist_video(id, data);
    console.log(result);
  },

  deleteTemplate: async (id) => {
    const result = await deleteChecklist(id);
    set((state) => ({
      checklist: state.checklist.filter((item) => item.checklistId !== id),
    }));
  },
});
