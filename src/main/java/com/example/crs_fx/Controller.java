package com.example.crs_fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private BorderPane loginForm;

    @FXML
    private BorderPane regForm;
    @FXML
    private Button loginBnt;

    @FXML
    private Button loginCreateAccount;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private TextField loginUserName;

    @FXML
    private Button signUpBnt;

    @FXML
    private Button signUpLogin;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private TextField signUpUserName;

    @FXML
    private TextField signUpRole;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    static String name = "None";


    public void loginAccount() {
        String sql = "SELECT username, password, role from Users WHERE username = ? AND password = ?";

        connection = Database.connect();

        try {
            Alert alert;
            if (loginUserName.getText().isEmpty() || loginPassword.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blanks");
                alert.showAndWait();
            } else {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, loginUserName.getText());
                preparedStatement.setString(2, loginPassword.getText());

                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully login");
                    alert.showAndWait();

                    name = loginUserName.getText();

                    loginBnt.getScene().getWindow().hide();

                    String role = resultSet.getString("role");
                    FXMLLoader fxmlLoader;
                    if (role.equals("Student")) {
                        fxmlLoader = new FXMLLoader(getClass().getResource("Student.fxml"));
                    } else if (role.equals("Admin")) {
                        fxmlLoader = new FXMLLoader(getClass().getResource("Admin.fxml"));
                    } else {
                        fxmlLoader = new FXMLLoader(getClass().getResource("Staff.fxml"));
                    }
                    Stage stage = new Stage();
                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setScene(scene);
                    stage.show();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username or Password");
                    alert.showAndWait();
                }

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void registrationAccount() {

        String sql = "INSERT INTO users(username, password, role) values(?,?,?)";

        connection = Database.connect();

        try {
            Alert alert;
            if (signUpUserName.getText().isEmpty() || signUpPassword.getText().isEmpty() || signUpRole.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blanks");
                alert.showAndWait();
            } else if (!signUpRole.getText().equals("Staff") && !signUpRole.getText().equals("Student")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please choose only Staff or Student role");
                alert.showAndWait();
            } else {

                String sqlCheck = "SELECT username FROM users WHERE username = '" + signUpUserName.getText() + "'";
                preparedStatement = connection.prepareStatement(sqlCheck);

                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Username has already taken");
                    alert.showAndWait();

                } else {
                    preparedStatement = connection.prepareStatement(sql);

                    preparedStatement.setString(1, signUpUserName.getText());
                    preparedStatement.setString(2, signUpPassword.getText());
                    preparedStatement.setString(3, signUpRole.getText());

                    preparedStatement.executeUpdate();


                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully registration");
                    alert.showAndWait();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchForm(ActionEvent e) {
        if (e.getSource() == signUpLogin) {
            loginForm.setVisible(true);
            regForm.setVisible(false);
        } else if (e.getSource() == loginCreateAccount) {
            loginForm.setVisible(false);
            regForm.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
