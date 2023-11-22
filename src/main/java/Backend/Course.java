package Backend;

import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {

    private String courseName;
    private int courseID;
    private int maxQuantityRegister;
    private List<String> nameOfStudent;
    private String courseInstructor;
    private String courseSection;
    private String courseLocation;

    public Course(int courseID, String courseName, int maxQuantityRegister, String courseInstructor, String courseSection, String courseLocation) {
        this.courseName = courseName;
        this.courseID = courseID;
        this.maxQuantityRegister = maxQuantityRegister;
        this.courseInstructor = courseInstructor;
        this.courseSection = courseSection;
        this.courseLocation = courseLocation;
    }


    public String getCourseName() {
        return courseName;
    }

    public int getCourseID() {
        return courseID;
    }

    public int getMaxQuantityRegister() {
        return maxQuantityRegister;
    }

    public List<String> getNameOfStudent() {
        return nameOfStudent;
    }

    public String getCourseInstructor() {
        return courseInstructor;
    }

    public String getCourseSection() {
        return courseSection;
    }

    public String getCourseLocation() {
        return courseLocation;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public void setMaxQuantityRegister(int maxQuantityRegister) {
        this.maxQuantityRegister = maxQuantityRegister;
    }

    public void setNameOfStudent(List<String> nameOfStudent) {
        this.nameOfStudent = nameOfStudent;
    }

    public void setCourseInstructor(String courseInstructor) {
        this.courseInstructor = courseInstructor;
    }

    public void setCourseSection(String courseSection) {
        this.courseSection = courseSection;
    }

    public void setCourseLocation(String courseLocation) {
        this.courseLocation = courseLocation;
    }

    @Override
    public String toString() {
        return courseName + " " + courseID + " " + maxQuantityRegister + " " + courseInstructor + " " + courseSection + " " + courseLocation;
    }
}
