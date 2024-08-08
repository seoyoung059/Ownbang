import { useState, useEffect } from "react";
import { Container, Box } from "@mui/material";
import CheckListTitle from "./CheckListTitle";
import CheckListList from "./CheckListList";
import CheckListAddInput from "./CheckListAddInput";
import { useBoundStore } from "../../store/store";

const CheckList = ({ canEdit, onTimestampClick }) => {
  const {
    checklist,
    fetchCheckLists,
    addNewTemplate,
    modifyCheckLists,
    deleteTemplate,
    stampCheckList,
  } = useBoundStore((state) => ({
    checklist: state.checklist,
    fetchCheckLists: state.fetchCheckLists,
    addNewTemplate: state.addNewTemplate,
    deleteTemplate: state.deleteTemplate,
    modifyCheckLists: state.modifyCheckLists,
    stampCheckList: state.stampCheckList,
  }));

  const [selectedChecklist, setSelectedChecklist] = useState(null);
  const [selectedTitle, setSelectedTitle] = useState("");

  useEffect(() => {
    fetchCheckLists();
  }, [fetchCheckLists]);

  useEffect(() => {
    if (selectedTitle === "") {
      setSelectedChecklist(null);
    } else {
      const selected = checklist.find((item) => item.title === selectedTitle);
      if (selected) {
        setSelectedChecklist(selected);
      }
    }
  }, [selectedTitle, checklist]);

  const updateChecklistAndFetch = async (updatedChecklist) => {
    setSelectedChecklist(updatedChecklist);
    await modifyCheckLists(updatedChecklist.checklistId, {
      title: updatedChecklist.title,
      contents: updatedChecklist.contents,
    });
    fetchCheckLists();
  };

  const formatTimeDiff = (timeDiffMinutes) => {
    const minutes = Math.floor(timeDiffMinutes / 60);
    const seconds = timeDiffMinutes % 60;
    return `${String(minutes).padStart(2, "0")}:${String(seconds).padStart(
      2,
      "0"
    )}`;
  };

  const onCreate = async (newKey) => {
    const updatedChecklist = {
      ...selectedChecklist,
      contents: { ...selectedChecklist.contents, [newKey]: "" },
    };

    // contents를 업데이트하면서 새 항목을 마지막에 추가하도록 함
    setSelectedChecklist(updatedChecklist);

    await modifyCheckLists(updatedChecklist.checklistId, {
      title: updatedChecklist.title,
      contents: updatedChecklist.contents,
    });
  };

  const onUpdate = async (key) => {
    const createdAt = localStorage.getItem("createdAt");
    if (!createdAt) {
      console.error("createdAt 값이 로컬스토리지에 없습니다.");
      return;
    }

    const createdAtDate = new Date(parseInt(createdAt));
    const now = new Date().getTime();
    const timeDiffSeconds = Math.floor((now - createdAtDate) / 1000);

    const updatedChecklist = {
      ...selectedChecklist,
      contents: {
        ...selectedChecklist.contents,
        [key]: formatTimeDiff(timeDiffSeconds),
      },
    };

    console.log(formatTimeDiff(timeDiffSeconds));

    await updateChecklistAndFetch(updatedChecklist);
  };

  const onDelete = async (key) => {
    const { [key]: _, ...rest } = selectedChecklist.contents;
    const updatedChecklist = {
      ...selectedChecklist,
      contents: rest,
    };
    await updateChecklistAndFetch(updatedChecklist);
  };

  const addTemplate = async (title) => {
    const newTemplate = {
      title,
      contents: {},
    };
    await addNewTemplate(newTemplate);
    fetchCheckLists();
  };

  const modifyTemplateTitle = async (newTitle) => {
    await modifyCheckLists(selectedChecklist.id, {
      title: newTitle,
      contents: selectedChecklist.contents,
    });
    fetchCheckLists();
    setSelectedTitle(newTitle);
  };

  const handleTimestampClick = (timestamp) => {
    if (onTimestampClick) {
      onTimestampClick(timestamp); // 부모 컴포넌트의 핸들러 호출
    }
  };

  return (
    <Container
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        height: "630px",
      }}
    >
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          border: "1px solid lightGray",
          borderRadius: "10px",
          padding: "30px",
          height: "82%",
          maxWidth: "500px",
          width: "100%",
        }}
      >
        <Box sx={{ display: "flex", justifyContent: "center" }}>
          <CheckListTitle
            checklist={checklist}
            selectedTitle={selectedTitle}
            setSelectedTitle={setSelectedTitle}
            addTemplate={addTemplate}
            deleteTemplate={deleteTemplate}
            modifyTemplateTitle={modifyTemplateTitle}
          />
        </Box>

        {selectedChecklist && (
          <Box
            sx={{
              flexGrow: 1,
              overflowY: "auto",
              mt: "30px",
              height: "320px",
              "::-webkit-scrollbar": {
                display: "none",
              },
            }}
          >
            <CheckListList
              contents={selectedChecklist.contents}
              onUpdate={onUpdate}
              onDelete={onDelete}
              canEdit={canEdit}
              onTimestampClick={handleTimestampClick}
            />
          </Box>
        )}

        {selectedChecklist && (
          <Box sx={{ mt: "20px" }}>
            <CheckListAddInput onCreate={onCreate} />
          </Box>
        )}
      </Box>
    </Container>
  );
};

export default CheckList;
