package program.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import program.model.*;
import program.viewer.DisplayShelf;
import program.viewer.ImageApp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    public Button addTagBtn;
    public Button updateImageInfoBtn;
    public Button deleteTagBtn;
    public Button goBackBtn;
    public Button moveImgBtn;
    public Button showAllTagsBtn;
    public Button chooseDirectoryBtn;
    public TextField addTagTxt;
    public Label originalNameLbl;
    public Label currentNameLbl;
    public Label moveResultLbl;
    public Label tipsLbl;
    public Label pathLbl;
    public ListView<Object> tagList;
    public ListView<String> changeList;
    static ImageFile image = null;
    public Button moveToSafeBtn;
    public Button goToSafeBtn;
    static Boolean login = false;
    public Label logConditionFeedback;
    public Button goToSafeFolderBtn;
    public File file;
    public static String name;
    public Button logOutBtn;
    public Label loginStatus;

    public void initialize(URL location, ResourceBundle resources) {
        if (login) {
            loginStatus.setText("User logged in!");
        }
    }

    /**
     * Let userTxt choose directory in file browser
     */
    public void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        //Show open file dialog
        file = directoryChooser.showDialog(null);
        openDirectory();
    }

    /**
     * Display images in displayShelf.
     */
    private void openDirectory(){
        logConditionFeedback.setText("");
        if (file != null) {
            ImageApp.path = file.toString();
            ImageApp.images.clear();
            Directory directory = new Directory(file.toString());
            ImageApp.imageFiles = directory.getImageFile();
            int i = 0;
            while (i < ImageApp.imageFiles.size()) {
                ImageApp.images.add(new Image("file:" + ImageApp.imageFiles.get(i).getLocation()));
                i++;
            }
            //reload to display newly selected images
            ImageApp.root.setTop(null);
            ImageApp.root.setTop(ImageApp.createContent());
        }
        updateImageInfo();
    }
    /**
     * add the userTxt input in addTagTxt as a tag of image.
     */
    public void addTag() {
        updateImageInfo();
        if (image != null) {
            //handle situation when input contains empty string.
            if (addTagTxt.getText().contains(" ") || addTagTxt.getText().isEmpty()) {
                addTagTxt.clear();
                addTagTxt.setPromptText("Please Enter The Tag You Want To Add, Without Whitespaces.");
            //handle situation when input matches an existing tag .
            } else if (image.getTags().contains(TagManager.checkExists(addTagTxt.getText()))) {
                addTagTxt.clear();
                addTagTxt.setPromptText("Tag Already Existed. Please Try A New One.");
            } else {
                Tag T = TagManager.checkExists(addTagTxt.getText());
                new ImageEditor(image).addNewTag(T);
                addTagTxt.clear();
                addTagTxt.setPromptText("Tag Successfully Added! Click To Enter Another One.");
            }
        }
        updateImageInfo();
    }

    /**
     * Update the currentNameLbl to show current name of image.
     */
    public void updateImageInfo() {
        if (ImageApp.images.size() != 0) {
            logConditionFeedback.setText("");
            image = ImageApp.imageFiles.get(DisplayShelf.getcenter());
            currentNameLbl.setText(ImageApp.imageFiles.get(DisplayShelf.getcenter()).getImageName());
            showTags();
            showOriginalName();
            getPath();
            showChanges();
        }

    }

    /**
     * show log of the image in changeList.
     */
    private void showChanges() {
        if (image != null) {
            changeList.getItems().clear();
            ArrayList<ArrayList<String>> change = Log.getInstance().getLogs(image);
            for (ArrayList<String> aChange : change) {
                changeList.getItems().add(aChange.toString());
            }
            changeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        }
    }

    /**
     * Go back to the set of tags userTxt selected in changeList.
     */
    public void goBack() {
        if (image != null) {
            //Get the userTxt selection from changeList.
            String current = changeList.getSelectionModel().getSelectedItem();
            if (current != null) {
                String[] parts = current.split(",");
                new ImageEditor(image).goBack(parts[2].substring(1, parts[2].length() - 1));
            }
        }
        updateImageInfo();
    }

    /**
     * Delete the tag(s) userTxt selected in tagList.
     */
    public void deleteTag() {
        if (image != null) {
             if (tagList.getSelectionModel().getSelectedItems()!= null){
                ObservableList<Object> tags = tagList.getSelectionModel().getSelectedItems();
                 for (Object tag : tags) {
                     Tag T = TagManager.checkExists(tag.toString());
                     new ImageEditor(image).deleteTag(T);
                 }
            }
        }
        updateImageInfo();
    }

    /**
     * Display all tags of the image in tagList.
     */
    private void showTags() {
        if (image != null) {
            tagList.getItems().clear();
            ArrayList<Tag> tags = image.getTags();
            for (Tag tag : tags) {
                tagList.getItems().add(tag.getTagName());
            }
            tagList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
    }

    /**
     * Show the original name of image in originalNameLbl.
     */
    private void showOriginalName() {
        if (image != null) {
            originalNameLbl.setText(image.getOriginalName());
        }
    }

    /**
     * Let userTxt move image to a new directory, update moveResultLbl
     * and tipsLbl to show result of move and tips.
     */
    public void moveImage() {
        logConditionFeedback.setText("");
        updateImageInfo();
        moveResultLbl.setText("");
        tipsLbl.setText("");
        if (image != null) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            //show choose directory dialog.
            File file = directoryChooser.showDialog(null);
            if (file != null) {
                new Directory(ImageApp.path).moveFile(image, file.toString());
                moveResultLbl.setText("move successful");
                tipsLbl.setText("To view the moved image, please choose your file again");
            }
        }
        openDirectory();
    }

    /**
     * Show the path of image in pathLbl.
     */
    private void getPath() {
        if (image != null) {
            String path = image.getLocation();
            pathLbl.setText(path);
        }
    }

    /**
     * Show tag set in a new window.
     */
    public void showTagSet() {
        logConditionFeedback.setText("");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/program/viewer/TagsBuilder.fxml"));
        try {
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setTitle("View Tag Set");
                stage.setScene(scene);
                stage.show();
        } catch (IOException e) {
                System.out.println("TagsBuilder not found!");
        }
    }

    /**
     * Move image to Safe Image stored in SafeBox.
     */
    public void moveToSafe(ActionEvent actionEvent) {
        if (login){
            logConditionFeedback.setText(null);
            SafeBox.getInstance().addSafeImage(image,name);
            openDirectory();
        }else{
            logConditionFeedback.setText("Please login first!");
        }
    }

    /**
     * Open the login in window is user has not logged in.
     */
    public void goToSafe(ActionEvent actionEvent) {
        if(!login) {
            logConditionFeedback.setText("");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/program/viewer/SafeBuilder.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setTitle("login Your Account");
                stage.setScene(scene);
                stage.show();
                logConditionFeedback.setText(null);
            } catch (IOException e) {
                System.out.println("SafeBuilder not found!");
            }
        }else{
            logConditionFeedback.setText("Already logged in!");
        }
    }

    /**
     * Let the user enter safe folder.
     */
    public void goToSafeFolder(ActionEvent actionEvent) {
        if (login){
            logConditionFeedback.setText("");
            Directory directory = new Directory("safebox/" + name);
            ImageApp.images.clear();
            ImageApp.imageFiles = directory.getImageFile();
            int i = 0;
            while (i < ImageApp.imageFiles.size()) {
                ImageApp.images.add(new Image("file:" + ImageApp.imageFiles.get(i).getLocation()));
                i++;
            }
            //reload to display newly selected images
            ImageApp.root.setTop(null);
            ImageApp.root.setTop(ImageApp.createContent());
            updateImageInfo();
        } else {
            logConditionFeedback.setText("Please login first!");
        }
    }

    /**
     * Let user log out from safeBox account
     */
    public void logOut() {
        if (login) {
            logConditionFeedback.setText("");
            login = false;
            logConditionFeedback.setText("Log out successful!");
        }
        else {
            logConditionFeedback.setText("Please login first!");
        }
    }
}