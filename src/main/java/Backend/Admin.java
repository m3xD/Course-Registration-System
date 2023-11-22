package Backend;

import IO.FileIO;
import IO.ObjectIO;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Admin extends User implements AdminDao {
    Scanner scanner = new Scanner(System.in);

    public Admin(String userName, String password, String firstName, String midName, String lastName) {
        super(userName, password, firstName, midName, lastName);
        courses = new ArrayList<>();
    }

    public void adminMenu() throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean isSelected = false;
        while (!isSelected) {
            System.out.println("Make your choice?");
            System.out.println("1. Create new course");
            System.out.println("2. Delete course");
            System.out.println("3. Edit course");
            System.out.println("4. Display course");
            System.out.println("5. Register student");
            System.out.println("6. Show all course");
            System.out.println("7. View all full course");
            System.out.println("8. View name of student");
            System.out.println("9. View list of course");
            System.out.println("10. Sort all courses");
            System.out.println("11. Exit");
            int otp = scanner.nextInt();
            scanner.nextLine();
            switch (otp) {
                case 1 -> createNewCourse();
                case 2 -> deleteCourse();
                case 3 -> editCourse();
                case 4 -> displayCourse();
                case 5 -> registerStudent();
                case 6 -> viewAllCourse();
                case 7 -> viewAllFullCourse();
                case 8 -> viewNameOfStudent();
                case 9 -> viewListOfCourse();
                case 10 -> sortCourse();
                case 11 -> {
                    isSelected = true;
                    System.out.println("Goodbye, good to see you then!");
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    List<Course> courses;

    // 1
    public void createNewCourse() throws Exception {
        Course course = collectInformation();
        courses.add(course);
        FileIO.writeFile("course.txt", course.toString());
        new FileWriter("courseArray.txt", false).close();
        ObjectIO.WriteObjectToFile(courses, "courseArray.txt");
    }

    // 2
    public void deleteCourse() throws Exception {
        Course course = collectInformation();
        StringBuilder stringBuilder = new StringBuilder();
        for (Course i : courses) {
            if (!i.equals(course)) {
                stringBuilder.append(i).append("\n");
            } else {
                courses.remove(i);
            }
        }
        new FileWriter("course.txt", false).close();
        FileIO.writeFile("course.txt", stringBuilder.toString());
        new FileWriter("courseArray.txt", false).close();
        ObjectIO.WriteObjectToFile(courses, "courseArray.txt");
    }
    // 3

    public void editCourse() throws Exception {
        Course course = collectInformation();
        System.out.println("Which information you want to edit?");
        System.out.println("1. Max quantity of course");
        System.out.println("2. Course instructor");
        System.out.println("3. Course section");
        System.out.println("4. Course location");
        Scanner scanner = new Scanner(System.in);
        Course tmp = new Course(course.getCourseID(), course.getCourseName(), course.getMaxQuantityRegister(), course.getCourseInstructor(), course.getCourseSection(),
                course.getCourseLocation());
        int otp = Integer.parseInt(scanner.next());
        switch (otp) {
            case 1 -> {
                System.out.println("Enter new quantity");
                course.setMaxQuantityRegister(scanner.nextInt());
            }
            case 2 -> {
                System.out.println("Enter new course instructor name");
                course.setCourseInstructor(scanner.nextLine());
            }
            case 3 -> {
                System.out.println("Enter new course section");
                course.setCourseSection(scanner.nextLine());
            }
            case 4 -> {
                System.out.println("Enter new course location");
                course.setCourseLocation(scanner.nextLine());
            }
            default -> System.out.println("Invalid option");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Course i : courses) {
            if (!i.equals(tmp)) {
                stringBuilder.append(i).append("\n");
            } else {
                stringBuilder.append(course).append("\n");
                courses.remove(tmp);
                courses.add(course);
            }
        }
        new FileWriter("course.txt", false).close();
        FileIO.writeFile("course.txt", stringBuilder.toString());
        new FileWriter("courseArray.txt", false).close();
        ObjectIO.WriteObjectToFile(courses, "courseArray.txt");
    }

    // 4

    public void displayCourse() {
        System.out.println("Enter courseId");
        int courseId = scanner.nextInt();
        for (var i : courses) {
            if (i.getCourseID()==courseId) {
                System.out.println(i);
                break;
            }
        }
    }

    // 5

    public void registerStudent() {
        Student student = collectInformationStudent();
        System.out.println("Enter course ID");
        int courseID = scanner.nextInt();
        for (Course i : courses) {
            if (i.getCourseID() == (courseID)) {
                i.getNameOfStudent().add(student.getFirstName() + student.getMidName() + student.getLastName());
            }
        }
    }

    // 6
    public void viewAllCourse() {
        System.out.println("List all course");
        for (var i : courses) {
            System.out.println(i);
        }
    }

    // 7
    public void viewAllFullCourse() {
        System.out.println("List of course has full quantity");
        for (var i : courses) {
            if (i.getMaxQuantityRegister() == i.getNameOfStudent().size()) {
                System.out.println(i);
            }
        }
    }

    // 8
    public void viewNameOfStudent() {
        Course course = collectInformation();
        for (var i : courses) {
            if (i.equals(course)) {
                for (var j : i.getNameOfStudent()) {
                    System.out.println(j);
                }
            }
        }
    }

    public void viewListOfCourse() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your first name");
        String firstName = scanner.next();
        System.out.println("Enter your last name");
        String lastName = scanner.next();
        for (var i : courses) {
            for (var j : i.getNameOfStudent()) {
                String words[] = j.split("\\s+");
                if (words[0].equals(firstName) && words[2].equals(lastName)) {
                    System.out.println(i);
                    break;
                }
            }
        }
    }

    public void sortCourse() throws Exception {
        courses.sort(Comparator.comparingInt(o -> o.getNameOfStudent().size()));
        System.out.println("Course has been sorted");
        new FileWriter("courseArray.in", false).close();
        ObjectIO.WriteObjectToFile(courses, "courseArray.txt");
    }


}

interface AdminDao {
    public void createNewCourse() throws Exception;

    public void deleteCourse() throws Exception;

    public void editCourse() throws Exception;

    public void displayCourse();

    public void registerStudent();

    public void viewAllCourse();

    public void viewAllFullCourse();

    public void viewNameOfStudent();

    public void viewListOfCourse();

    public void sortCourse() throws Exception;

}
