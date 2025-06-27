package org.example;

import org.bson.Document;
import com.mongodb.client.MongoIterable;

class DisplayFormatter {
    public static void printCourses(MongoIterable<Document> courses) {
        int counter = 1;
        System.out.println("\n" + "=".repeat(100));
        for (Document c : courses) {
            System.out.printf("\nCourse #%d\n", counter++);
            System.out.println("-".repeat(100));
            System.out.printf("%-18s : %s\n", "Course ID", c.getString("courseId"));
            System.out.printf("%-18s : %s\n", "Course Name", c.getString("courseName"));
            System.out.printf("%-18s : %d\n", "Available Seats", c.getInteger("availableSeats"));
            System.out.println("-".repeat(100));
        }
        System.out.println("=".repeat(100));
    }
}