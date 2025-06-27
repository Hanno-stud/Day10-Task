// Latest Production-Ready Main.java for Elective System
// Includes MongoDB log management, professional output, and complete feature integration

package org.example;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        MongoLogger.startCapture();  // Mongo Logs are captured and buffered.

        // === STEP 1: Capture MongoDB logs ===
        PrintStream originalErr = System.err;
        ByteArrayOutputStream mongoLogs = new ByteArrayOutputStream();
        System.setErr(new PrintStream(mongoLogs));

        ElectiveSystem service = new ElectiveSystem();
        Scanner sc = new Scanner(System.in);

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

            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    ElectiveSystem.registerStudent();
                    break;
                case "2":
                    ElectiveSystem.studentLogin();
                    break;
                case "3":
                    ElectiveSystem.viewCourses();
                    break;
                case "4":
                    ElectiveSystem.registerForCourse();
                    break;
                case "5":
                    ElectiveSystem.addNewCourse();
                    break;
                case "6":
                    ElectiveSystem.viewAllStudents();
                    break;
                case "7":
                    System.out.println("Exiting system. Goodbye!");
                    ElectiveSystem.shutdown();

                    // === STEP 2: Flush MongoDB Logs ===
                    System.out.println("\nMongo Logs (captured and displayed after program execution):");
                    System.out.println("-".repeat(100));
                    MongoLogger.flushLogs();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
