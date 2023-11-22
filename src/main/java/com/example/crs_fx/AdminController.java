package com.example.crs_fx;

import Backend.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
     @FXML
    private Button addnewCourse_Admin;

    @FXML
    private TableColumn<Course, String> adminCourseID;

    @FXML
    private TableColumn<Course, String> adminCourseLocation;

    @FXML
    private TableColumn<Course, String> adminCourseName;

    @FXML
    private TableColumn<Course, String> adminCourseSec;

    @FXML
    private TableColumn<Course, String> adminInstruction;

    @FXML
    private TableColumn<Course, String> adminMax;

    @FXML
    private Label adminRole;

    @FXML
    private TableView<Course> adminTableView;

    @FXML
    private TextField courseIDAdmin;

    @FXML
    private TextField courseInstructorAdmin;

    @FXML
    private TextField courseLocationAdmin;

    @FXML
    private TextField courseNameAdmin;

    @FXML
    private TextField courseSectionAdmin;

    @FXML
    private Button deleteCourse_Admin;

    @FXML
    private Button logoutBnt_Admin;

    @FXML
    private TextField maxQuantityAdmin;

    @FXML
    private Label nameAdmin;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public void setAddnewCourse_Admin() {
        String sql = "INSERT INTO Courses(id, name, max, courseSection, courseLocation, adminID) values (?, ?, ?, ?, ?, 1)";
        try {
            connection = Database.connect();

            if (courseIDAdmin.getText().isEmpty() || courseNameAdmin.getText().isEmpty()
                    || maxQuantityAdmin.getText().isEmpty()
                    || courseSectionAdmin.getText().isEmpty() || courseLocationAdmin.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank");
                alert.showAndWait();
            } else {
                String checkData = "SELECT name FROM Courses WHERE name = ?";

                preparedStatement = connection.prepareStatement(checkData);
                preparedStatement.setString(1, courseNameAdmin.getText());
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Course name has already existed");
                    alert.showAndWait();
                } else {
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, courseIDAdmin.getText());
                    preparedStatement.setString(2, courseNameAdmin.getText());
                    preparedStatement.setString(3, maxQuantityAdmin.getText());
                    preparedStatement.setString(4, courseSectionAdmin.getText());
                    preparedStatement.setString(5, courseLocationAdmin.getText());

                    preparedStatement.executeUpdate();


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully add course");
                    alert.showAndWait();

                    adminShowData();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void logOut() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to log out?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            logoutBnt_Admin.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("User choose cancel");
        }
    }

    public void setDeleteCourse_Admin() {
        connection = Database.connect();

        try {
            String sql1 = "DELETE FROM register r WHERE r.courseID = (SELECT id FROM Courses c WHERE c.name = ?)";
            String sql2 = "DELETE FROM Courses WHERE name = ?";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to delete this course?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Course c = adminTableView.getSelectionModel().getSelectedItem();
                preparedStatement = connection.prepareStatement(sql1);
                preparedStatement.setString(1, c.getCourseName());
                preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement(sql2);
                preparedStatement.setString(1, c.getCourseName());

                preparedStatement.executeUpdate();

                adminShowData();
            } else {
                System.out.println("User choose cancel");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Course> adminList() {
        ObservableList<Course> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM Courses";
        connection = Database.connect();
        try {

            preparedStatement = connection.prepareStatement(query);


            String q = "select s.name, users.role from Admin s JOIN users on s.userID = users.id where users.id = 1";
            PreparedStatement preparedStatement1 = connection.prepareStatement(q);

            ResultSet r = preparedStatement1.executeQuery();
            String name = null;
            String role = null;
            if (r.next()) {
                name = r.getString("s.name");
                role = r.getString("users.role");
            }
            nameAdmin.setText(name);
            adminRole.setText(role);


            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Course(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getInt("max"), "none",
                        resultSet.getString("courseSection"), resultSet.getString("courseLocation")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private ObservableList<Course> courses;

    public void adminShowData() {
        courses = adminList();

        adminCourseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        adminCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        adminMax.setCellValueFactory(new PropertyValueFactory<>("maxQuantityRegister"));
        adminCourseSec.setCellValueFactory(new PropertyValueFactory<>("courseSection"));
        adminCourseLocation.setCellValueFactory(new PropertyValueFactory<>("courseLocation"));

        adminTableView.setItems(courses);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adminShowData();
    }
}
