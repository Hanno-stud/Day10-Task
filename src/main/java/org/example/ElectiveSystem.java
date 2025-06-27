// Full Production-Ready Elective Course Registration System with Persistent Course Management and Student View Option

package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.*;

public class ElectiveSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private static final MongoDatabase database = mongoClient.getDatabase("electiveSystem");
    private static final MongoCollection<Document> students = database.getCollection("students");
    private static final MongoCollection<Document> courses = database.getCollection("courses");

    public static void main(String[] args) {
        insertSampleCourses();
        while (true) {
            System.out.println("\n--- ELECTIVE COURSE REGISTRATION SYSTEM ---");
            System.out.println("1. Register as Student");
            System.out.println("2. Student Login");
            System.out.println("3. View Available Courses");
            System.out.println("4. Register for a Course");
            System.out.println("5. Add New Course");
            System.out.println("6. View All Registered Students");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": registerStudent(); break;
                case "2": studentLogin(); break;
                case "3": viewCourses(); break;
                case "4": registerForCourse(); break;
                case "5": addNewCourse(); break;
                case "6": viewAllStudents(); break;
                case "7":
                    System.out.println("Exiting system. Goodbye!");
                    mongoClient.close();
                    return;
                default: System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void insertSampleCourses() {
        if (courses.countDocuments() == 0) {
            List<Document> sampleCourses = List.of(
                    new Document("courseId", "C101").append("courseName", "Data Science Basics"),
                    new Document("courseId", "C102").append("courseName", "Cloud Computing Essentials"),
                    new Document("courseId", "C103").append("courseName", "AI & Machine Learning"),
                    new Document("courseId", "C201").append("courseName", "Advanced Artificial Intelligence"),
                    new Document("courseId", "C202").append("courseName", "Machine Learning Specialization"),
                    new Document("courseId", "C203").append("courseName", "Cybersecurity and Ethical Hacking"),
                    new Document("courseId", "C204").append("courseName", "Data Science for Technology Leaders"),
                    new Document("courseId", "C205").append("courseName", "Cloud Computing and DevOps")
            );
            courses.insertMany(sampleCourses);
            System.out.println("Sample courses inserted successfully.");
        }
    }

    static void registerStudent() {
        System.out.print("Enter your Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty() || students.find(Filters.eq("email", email)).first() != null) {
            System.out.println("Email is either invalid or already registered.");
            return;
        }

        String password = generatePassword();

        try {
            System.out.println("[INFO] Password for login: " + password);

            Document student = new Document("email", email)
                    .append("password", password)
                    .append("courses", new ArrayList<String>());

            students.insertOne(student);
            System.out.println("Student registered successfully.");
        } catch (Exception e) {
            System.out.println("Registration failed.");
            e.printStackTrace();
        }
    }

    static boolean studentLogin() {
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        Document student = students.find(Filters.and(
                Filters.eq("email", email),
                Filters.eq("password", password)
        )).first();

        if (student != null) {
            System.out.println("Login successful. Welcome " + email + "!");
            return true;
        } else {
            System.out.println("Invalid credentials.");
            return false;
        }
    }

    static void viewCourses() {
        FindIterable<Document> courseList = courses.find();
        List<Document> allCourses = new ArrayList<>();
        courseList.into(allCourses);

        if (allCourses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }

        System.out.println("\nAVAILABLE COURSES:");
        System.out.println("=".repeat(60));
        for (Document c : allCourses) {
            System.out.println("Course ID: " + c.getString("courseId") + " | Course Name: " + c.getString("courseName"));
        }
        System.out.println("=".repeat(60));
    }

    static void registerForCourse() {
        System.out.print("Enter your registered Email: ");
        String email = scanner.nextLine().trim();
        Document student = students.find(Filters.eq("email", email)).first();

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        viewCourses();
        System.out.print("Enter Course ID to register: ");
        String courseId = scanner.nextLine().trim();

        Document course = courses.find(Filters.eq("courseId", courseId)).first();
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        List<String> currentCourses = student.getList("courses", String.class);
        if (currentCourses.contains(courseId)) {
            System.out.println("You are already registered for this course.");
            return;
        }

        UpdateResult result = students.updateOne(
                Filters.eq("email", email),
                Updates.addToSet("courses", courseId)
        );

        if (result.getModifiedCount() > 0) {
            System.out.println("Successfully registered for course: " + course.getString("courseName"));
        } else {
            System.out.println("Course registration failed.");
        }
    }

    static void addNewCourse() {
        System.out.print("Enter new Course ID: ");
        String courseId = scanner.nextLine().trim();

        if (courses.find(Filters.eq("courseId", courseId)).first() != null) {
            System.out.println("Course ID already exists.");
            return;
        }

        System.out.print("Enter Course Name: ");
        String courseName = scanner.nextLine().trim();

        Document newCourse = new Document("courseId", courseId)
                .append("courseName", courseName);

        courses.insertOne(newCourse);
        System.out.println("New course added successfully.");
    }

    static void viewAllStudents() {
        FindIterable<Document> studentList = students.find();
        List<Document> allStudents = new ArrayList<>();
        studentList.into(allStudents);

        if (allStudents.isEmpty()) {
            System.out.println("No students registered.");
            return;
        }

        System.out.println("\nREGISTERED STUDENTS:");
        System.out.println("=".repeat(60));
        for (Document s : allStudents) {
            System.out.println("Email: " + s.getString("email") + " | Courses: " + s.getList("courses", String.class));
        }
        System.out.println("=".repeat(60));
    }

    private static String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 17; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
    public static void shutdown() {
        mongoClient.close();
    }

}
