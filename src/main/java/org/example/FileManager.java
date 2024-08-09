package org.example;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;
import java.util.logging.*;
import java.util.stream.Stream;

public class FileManager {
    // Logger for recording error messages
    private static final Logger logger = Logger.getLogger(FileManager.class.getName());

    public static void main(String[] args) {
        // Define the file system and directory to work with
        FileSystem fs = FileSystems.getDefault();
        Path dir = fs.getPath("C:\\Users\\User\\Carrie\\SimpleFileManagerApp\\src\\main\\resources\\FileHub");

        // Initialize Scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Main loop for user interaction
        while (true) {
            System.out.println("File Manager Options:");
            System.out.println("1. Display Directory Contents");
            System.out.println("2. Copy File");
            System.out.println("3. Move File");
            System.out.println("4. Delete File");
            System.out.println("5. Create Directory");
            System.out.println("6. Delete Directory");
            System.out.println("7. Search Files");
            System.out.println("8. Exit");

            int choice = scanner.nextInt(); // reads the integer value
            scanner.nextLine();  // Consumes the newline character left in the buffer

            switch (choice) {
                case 1:
                    displayDirectoryContents(dir);
                    break;
                case 2:
                    System.out.println("Enter source file name:");
                    Path source = dir.resolve(scanner.nextLine());
                    System.out.println("Enter target file name:");
                    Path target = dir.resolve(scanner.nextLine());
                    copyFile(source, target);
                    break;
                case 3:
                    System.out.println("Enter source file name:");
                    source = dir.resolve(scanner.nextLine());
                    System.out.println("Enter target file name:");
                    target = dir.resolve(scanner.nextLine());
                    moveFile(source, target);
                    break;
                case 4:
                    System.out.println("Enter file name to delete:");
                    Path fileToDelete = dir.resolve(scanner.nextLine());
                    deleteFile(fileToDelete);
                    break;
                case 5:
                    System.out.println("Enter directory name to create:");
                    Path newDir = dir.resolve(scanner.nextLine());
                    createDirectory(newDir);
                    break;
                case 6:
                    System.out.println("Enter directory name to delete:");
                    Path dirToDelete = dir.resolve(scanner.nextLine());
                    deleteDirectory(dirToDelete);
                    break;
                case 7:
                    System.out.println("Enter search term:");
                    String searchTerm = scanner.nextLine();
                    searchFiles(dir, searchTerm);
                    break;
                case 8:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the contents of a specified directory.
     * @param dir The path to the directory.
     */
    public static void displayDirectoryContents(Path dir) {
        try (Stream<Path> stream = Files.list(dir)) {
            stream.forEach(path -> {
                try {
                    BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                    System.out.printf("Name: %s, Size: %d bytes, Last Modified: %s%n",
                            path.getFileName(), attrs.size(), attrs.lastModifiedTime());
                } catch (IOException e) {
                    System.err.println("Error reading attributes: " + e.getMessage());
                }
            });
        } catch (NoSuchFileException e) {
            System.err.println("Directory not found: " + dir);
        } catch (IOException e) {
            System.err.println("Error listing directory contents: " + e.getMessage());
        }
    }

    /**
     * Copies a file from source to target.
     * @param source The path to the source file.
     * @param target The path to the target file.
     */
    public static void copyFile(Path source, Path target) {
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully!");
        } catch (NoSuchFileException e) {
            System.err.println("Source file not found: " + source);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error copying file", e);
        }
    }

    /**
     * Moves a file from source to target.
     * @param source The path to the source file.
     * @param target The path to the target file.
     */
    public static void moveFile(Path source, Path target) {
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved successfully!");
        } catch (NoSuchFileException e) {
            System.err.println("Source file not found: " + source);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error moving file", e);
        }
    }

    /**
     * Deletes a specified file.
     * @param path The path to the file to delete.
     */
    public static void deleteFile(Path path) {
        try {
            Files.delete(path);
            System.out.println("File deleted successfully!");
        } catch (NoSuchFileException e) {
            System.err.println("File not found: " + path);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error deleting file", e);
        }
    }

    /**
     * Creates a new directory.
     * @param dir The path to the directory to create.
     */
    public static void createDirectory(Path dir) {
        try {
            Files.createDirectory(dir);
            System.out.println("Directory created successfully!");
        } catch (FileAlreadyExistsException e) {
            System.err.println("Directory already exists: " + dir);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error creating directory", e);
        }
    }

    /**
     * Deletes a specified directory.
     * @param dir The path to the directory to delete.
     */
    public static void deleteDirectory(Path dir) {
        try {
            Files.delete(dir);
            System.out.println("Directory deleted successfully!");
        } catch (NoSuchFileException e) {
            System.err.println("Directory not found: " + dir);
        } catch (DirectoryNotEmptyException e) {
            System.err.println("Directory is not empty: " + dir);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error deleting directory", e);
        }
    }

    /**
     * Searches for files in the directory that match the search term.
     * @param dir The path to the directory to search.
     * @param searchTerm The term to search for in file names.
     */
    public static void searchFiles(Path dir, String searchTerm) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*" + searchTerm + "*")) {
            boolean found = false;
            for (Path path : stream) {
                System.out.println("Found: " + path.getFileName());
                found = true;
            }
            if (!found) {
                System.out.println("No files found matching the search term: " + searchTerm);
            }
        } catch (NoSuchFileException e) {
            System.err.println("Directory not found: " + dir);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error searching files", e);
        }
    }
}
