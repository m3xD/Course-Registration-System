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
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentController implements Initializable {

    @FXML
    private Button studentBnt;

    @FXML
    private TableColumn<Course, Integer> studentCourseID;

    @FXML
    private TableColumn<Course, String> studentCourseLocation;

    @FXML
    private TableColumn<Course, String> studentCourseName;

    @FXML
    private TableColumn<Course, String> studentCourseSec;

    @FXML
    private TableColumn<Course, String> studentInstruction;

    @FXML
    private TableColumn<Course, Integer> studentMax;

    @FXML
    private Button studentShowAllCourse;

    @FXML
    private Button studentWithdrawBnt;

    @FXML
    private TableView<Course> studentTableView;

    @FXML
    private Label nameStudent;

    @FXML
    private Label studentRole;


    public StudentController() {
    }

    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;

    public ObservableList<Course> studentList() {
        ObservableList<Course> list = FXCollections.observableArrayList();
        String query = "select c.id, c.name, c.max, S.name, c.courseSection, c.courseLocation from Courses c " +
                "JOIN register r on c.id = r.courseID JOIN Staffs S on r.staffID = S.id JOIN Students S2 on r.studentID = S2.id WHERE S2.name = ?";
        connection = Database.connect();
        try {

            preparedStatement = connection.prepareStatement(query);

            String s = Controller.name;
            String q = "select s.name, users.role from Students s JOIN users on s.userID = users.id where users.username = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(q);
            preparedStatement1.setString(1, s);
            ResultSet r = preparedStatement1.executeQuery();
            String v = null;
            String role = null;
            if (r.next()) {
                v = r.getString("s.name");
                role = r.getString("users.role");
            }
            nameStudent.setText(v);
            studentRole.setText(role);

            preparedStatement.setString(1, r.getString("s.name"));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(new Course(resultSet.getInt("c.id"), resultSet.getString("c.name"),
                        resultSet.getInt("c.max"), resultSet.getString("S.name"),
                        resultSet.getString("c.courseSection"), resultSet.getString("c.courseLocation")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private ObservableList<Course> courses;

    public void studentShowData() {
        courses = studentList();

        studentCourseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        studentCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        studentMax.setCellValueFactory(new PropertyValueFactory<>("maxQuantityRegister"));
        studentInstruction.setCellValueFactory(new PropertyValueFactory<>("courseInstructor"));
        studentCourseSec.setCellValueFactory(new PropertyValueFactory<>("courseSection"));
        studentCourseLocation.setCellValueFactory(new PropertyValueFactory<>("courseLocation"));

        studentTableView.setItems(courses);
    }

    public void logOut() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to log out?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            studentBnt.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("User choose cancel");
        }
    }



    public void withDraw() {

        connection = Database.connect();

        try {
            String tmp = "select s.id from Students s JOIN users u on s.userID = u.id WHERE u.username = ?";

            preparedStatement = connection.prepareStatement(tmp);
            preparedStatement.setString(1, Controller.name);
            resultSet = preparedStatement.executeQuery();
            String id = null;
            if (resultSet.next()) {
                id = resultSet.getString("id");
            }
            String sql = "DELETE FROM register r WHERE (r.courseID = ? AND r.studentID = ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, getSelected());
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully withdraw from course");
            alert.showAndWait();

            studentShowData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSelected() {
        Course c = studentTableView.getSelectionModel().getSelectedItem();
        int n = studentTableView.getSelectionModel().getSelectedIndex();
        if (n - 1 < -1) return null;
        return String.valueOf(c.getCourseID());
    }

    public void showAvailableCourse() throws Exception {
        studentShowAllCourse.getScene().getWindow().hide();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("availableCourse.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studentShowData();
    }
}
