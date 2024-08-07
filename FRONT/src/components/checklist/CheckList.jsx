import { useState, useEffect } from "react";
import { Container, Box } from "@mui/material";
import CheckListTitle from "./CheckListTitle";
import CheckListList from "./CheckListList";
import CheckListAddInput from "./CheckListAddInput";
import { useBoundStore } from "../../store/store";

const CheckList = ({ canEdit }) => {
  const {
    checklist,
    fetchCheckLists,
    addNewTemplate,
    modifyCheckLists,
    deleteTemplate,
  } = useBoundStore((state) => ({
    checklist: state.checklist,
    fetchCheckLists: state.fetchCheckLists,
    addNewTemplate: state.addNewTemplate,
    deleteTemplate: state.deleteTemplate,
    modifyCheckLists: state.modifyCheckLists,
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

  const onCreate = async (newKey) => {
    const updatedChecklist = {
      ...selectedChecklist,
      contents: { ...selectedChecklist.contents, [newKey]: "" },
    };
    setSelectedChecklist(updatedChecklist);
    await modifyCheckLists(updatedChecklist.checklistId, {
      title: updatedChecklist.title,
      contents: updatedChecklist.contents,
    });
  };

  const onUpdate = async (key) => {
    const updatedChecklist = {
      ...selectedChecklist,
      contents: {
        ...selectedChecklist.contents,
        [key]: selectedChecklist.contents[key] === "" ? "Checked" : "",
      },
    };
    setSelectedChecklist(updatedChecklist);
    await modifyCheckLists(updatedChecklist.checklistId, {
      title: updatedChecklist.title,
      contents: updatedChecklist.contents,
    });
  };

  const onDelete = async (key) => {
    const { [key]: _, ...rest } = selectedChecklist.contents;
    const updatedChecklist = {
      ...selectedChecklist,
      contents: rest,
    };
    setSelectedChecklist(updatedChecklist);
    await modifyCheckLists(updatedChecklist.checklistId, {
      title: updatedChecklist.title,
      contents: updatedChecklist.contents,
    });
  };

  const addTemplate = async (title) => {
    const newTemplate = {
      title,
      contents: {},
    };
    await addNewTemplate(newTemplate);
    fetchCheckLists();
  };

  return (
    <Container
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
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
            setSelectedTitle={setSelectedTitle}
            addTemplate={addTemplate}
            deleteTemplate={deleteTemplate}
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
