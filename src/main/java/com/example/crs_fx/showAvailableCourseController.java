package com.example.crs_fx;

import Backend.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class showAvailableCourseController implements Initializable {


    @FXML
    private Button addBntAvail;

    @FXML
    private Button goBackBnt;

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
    private TableView<Course> studentTableView;

    public void addButton() {

        connection = Database.connect();

        try {
            String sql = "INSERT INTO register(staffID, studentID, courseID, adminID) VALUE (?, ?, ?, ?)";
            Course c = studentTableView.getSelectionModel().getSelectedItem();
            int n = studentTableView.getSelectionModel().getSelectedIndex();

            // staffID
            String s1 = "SELECT id FROM Staffs WHERE name = ?";
            // StudentID
            String s2 = "select s.id from Students s JOIN users on s.userID = users.id where users.username = ?";
            // ans
            String r1 = null, r2 = null;

            preparedStatement = connection.prepareStatement(s1);
            preparedStatement.setString(1, c.getCourseInstructor());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) r1 = resultSet.getString("id");

            preparedStatement = connection.prepareStatement(s2);
            preparedStatement.setString(1, Controller.name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) r2 = resultSet.getString("s.id");

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, r1);
            preparedStatement.setString(2, r2);
            preparedStatement.setString(3, String.valueOf(c.getCourseID()));
            preparedStatement.setString(4, String.valueOf(1));

            preparedStatement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully register from course");
            alert.showAndWait();

            studentShowData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    public ObservableList<Course> studentList() {
        ObservableList<Course> list = FXCollections.observableArrayList();
        String query = "select c.id, c.name, c.max, S.name, c.courseSection, c.courseLocation from Courses c " +
                "JOIN register r on c.id = r.courseID " +
                "JOIN Staffs S on r.staffID = S.id " +
                "WHERE c.name NOT IN (SELECT name FROM Courses c JOIN register r2 on c.id = r2.courseID WHERE r2.studentID = ?)";
        connection = Database.connect();
        try {

            preparedStatement = connection.prepareStatement(query);

            String s = Controller.name;
            String q = "select s.id from Students s JOIN users on s.userID = users.id where users.username = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(q);
            preparedStatement1.setString(1, s);
            ResultSet r = preparedStatement1.executeQuery();
            String v = null;
            if (r.next()) {
                v = r.getString("s.id");
            }

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, v);
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

    public void goBack() throws Exception {
        goBackBnt.getScene().getWindow().hide();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Student.fxml"));
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
