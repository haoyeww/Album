package program.controller;


import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import program.model.Log;
import program.model.SafeBox;

public class SafeController {
    public TextField userTxt;
    public TextField passwordTxt;
    public Button loginBtn;
    public Button CreateAccountBtn;
    public Label feedbackLbl;

    /**
     * Sign the user in to use SafeBox given the user exists and
     * password is correct.
     */
    public void login(ActionEvent actionEvent) {
        feedbackLbl.setText("");
        if (! Log.getInstance().userExists(userTxt.getText())) {
            feedbackLbl.setText("User does not exist.");
        } else if (SafeBox.getInstance().passwordCheck(userTxt.getText(), passwordTxt.getText())) {
            Controller.name = userTxt.getText();
            Controller.login = true;
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.close();
        }else{
            feedbackLbl.setText("Your username or password is Wrong!");
        }
    }

    /**
     * Let user create a new account with chosen username and password.
     */
    public void createNewAccount() {
        feedbackLbl.setText("");
        if (Log.getInstance().userExists(userTxt.getText())) {
            feedbackLbl.setText("Username already exists. Pick a new one.");
        }else if (SafeBox.getInstance().passwordFormat(passwordTxt.getText())) {
            SafeBox.getInstance().addUser(userTxt.getText(), passwordTxt.getText());
            feedbackLbl.setText("Account successfully created! Please login.");
        }else{
            feedbackLbl.setText("Please follow the format for password.");
        }
    }
}
