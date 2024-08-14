import React, { useState, useEffect } from "react";
import { Container, Box, CircularProgress } from "@mui/material";
import CheckListTitle from "./CheckListTitle";
import CheckListList from "./CheckListList";
import CheckListAddInput from "./CheckListAddInput";
import { useBoundStore } from "../../store/store";

const CheckList = ({ reservationId, canEdit, onTimestampClick, forReplay }) => {
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
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchChecklists = async (id) => {
      setLoading(true);
      try {
        await fetchCheckLists(id);
      } finally {
        setLoading(false);
      }
    };

    fetchChecklists(reservationId);
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

  const updateChecklistAndFetch = async (reservationId, updatedChecklist) => {
    setSelectedChecklist(updatedChecklist);
    await modifyCheckLists(reservationId, {
      title: updatedChecklist.title,
      contents: updatedChecklist.contents,
    });
    fetchCheckLists(reservationId);
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

    await modifyCheckLists(reservationId, {
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

    await updateChecklistAndFetch(reservationId, updatedChecklist);
  };

  const onDelete = async (key) => {
    const { [key]: _, ...rest } = selectedChecklist.contents;
    const updatedChecklist = {
      ...selectedChecklist,
      contents: rest,
    };
    await updateChecklistAndFetch(reservationId, updatedChecklist);
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
    await modifyCheckLists(reservationId, {
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

  if (loading) {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          mt: 10,
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Container
      sx={{
        display: "flex",
        flexDirection: "column",
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
              forReplay={forReplay}
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
