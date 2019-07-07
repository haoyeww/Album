package program.controller;


import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import program.model.ImageEditor;
import program.model.ImageFile;
import program.model.Tag;
import program.model.TagManager;
import program.viewer.ImageApp;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TagsBuilderController implements Initializable {

    public ListView<java.io.Serializable> allTagsList;
    public Button addToAllBtn;
    public Button addBtn;
    public Button deleteBtn;
    public TextField addTagTxt;
    public Button addToThisBtn;
    public Label singleImgResult;
    public Label allImgResult;

    public void initialize(URL location, ResourceBundle resources) {
        allTagsList.getItems().clear();
        showTagSet();
    }

    /**
     * Display tag set in allTagsList.
     */
    private void showTagSet() {
        ArrayList<String> allTagsStr = new ArrayList<>();
        for (Tag tag : TagManager.allTags) {
            allTagsStr.add(tag.getTagName());
        }
        for (String tag : allTagsStr) {
            allTagsList.getItems().add(tag);
        }
        allTagsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Add the tag user selected in allTagList to the current selected image.
     */
    public void addTagToThisImg() {
        if (Controller.image != null) {
            if (allTagsList.getSelectionModel().getSelectedItems() != null) {
                ObservableList<java.io.Serializable> tags = allTagsList.getSelectionModel().getSelectedItems();
                for (Object tag : tags) {
                    Tag T = TagManager.checkExists(tag.toString());
                    new ImageEditor(Controller.image).addNewTag(T);
                }
                singleImgResult.setText("Tag(s) Successfully Added! ");
            }
        }
    }

    /**
     * Add the tag user selected in allTagList all images in selected directory.
     */
    public void addTagToAllImages() {
        if (ImageApp.imageFiles.size() != 0) {
            System.out.println(ImageApp.imageFiles);
            for (ImageFile img: ImageApp.imageFiles) {
                ObservableList<java.io.Serializable> tags = allTagsList.getSelectionModel().getSelectedItems();
                for (Object tag : tags) {
                    Tag T = TagManager.checkExists(tag.toString());
                    new ImageEditor(img).addNewTag(T);
                }
            }
            allImgResult.setText("Tag(s) Successfully Added To All Images! ");
        }
    }

    /**
     * Delete the tag user selected in allTagList from tag set.
     */
    public void delete() {
        if (allTagsList.getSelectionModel().getSelectedItems() != null) {
            ObservableList<java.io.Serializable> tags = allTagsList.getSelectionModel().getSelectedItems();
            for (Object tag : tags) {
                Tag t = TagManager.checkExists(tag.toString());
                TagManager.deleteTag(t);
            }
            allTagsList.getItems().clear();
            showTagSet();
        }
    }

    /**
     * Add tag to tag set given the input in addTagTxt
     * is valid and tag does not already exists.
     */
    public void add() {
        ArrayList<String> allTagsStr = new ArrayList<>();
        for (Tag tag : TagManager.allTags) {
            allTagsStr.add(tag.getTagName());
        }
        if (addTagTxt.getText().contains(" ") || addTagTxt.getText().isEmpty()) {
            addTagTxt.clear();
            addTagTxt.setPromptText("Please Enter The Tag You Want To Add, Without Whitespaces.");
            //handle situation when input matches an existing tag .
        } else if (allTagsStr.contains(addTagTxt.getText())) {
            addTagTxt.clear();
            addTagTxt.setPromptText("Tag Already Existed. Please Try A New One.");
        } else {
            Tag tag = TagManager.checkExists(addTagTxt.getText());
            addTagTxt.clear();
            addTagTxt.setPromptText("Tag Successfully Added!");
            allTagsList.getItems().clear();
            showTagSet();
        }
    }
}
