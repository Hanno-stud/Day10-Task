// MongoLogger.java
package org.example;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MongoLogger {
    private static PrintStream originalErr;
    private static ByteArrayOutputStream errBuffer;

    public static void startCapture() {
        originalErr = System.err;
        errBuffer = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errBuffer));
    }

    public static void flushLogs() {
        System.setErr(originalErr);
        String logs = errBuffer.toString();

        if (logs.isEmpty()) {
            System.out.println("\nMongo Logs: No logs captured during this session.");
            return;
        }

        String[] logEntries = logs.split("\\n");

        System.out.println("\nMongo Logs (Buffered and Delayed until Program Exit):");
        System.out.println("=".repeat(140));

        int counter = 1;
        for (String entry : logEntries) {
            if (!entry.trim().isEmpty()) {
                System.out.printf("[%02d] %s\n", counter++, entry.trim());
            }
        }

        System.out.println("=".repeat(140));
    }
}
