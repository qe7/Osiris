package io.github.qe7.utils.configs;

import io.github.qe7.utils.UtilityBase;

import java.io.File;
import java.nio.file.Files;

/**
 * Utility class for file/config related tasks
 * Files are stored in the Osiris/ directory in the root of the system drive
 */
public final class FileUtility extends UtilityBase {

    // File extension
    private static final String FILE_EXTENSION = ".json"; // We're using JSON files

    // Directory for files
    private static final String FILE_DIRECTORY = System.getenv("SystemDrive") + File.separator + "Osiris" + File.separator;

    /**
     * Creates the Osiris/ directory
     */
    public static void createDirectory() {
        File directory = new File(FILE_DIRECTORY);

        if (!directory.exists()) {
            try {
                if (directory.mkdirs()) {
                    System.out.println("Created directory");
                } else {
                    System.out.println("Failed to create directory");
                }
            } catch (Exception e) {
                System.out.println("Failed to create directory - " + e.getMessage());
            }
        } else {
            System.out.println("Directory already exists, skipping creation");
        }
    }

    /**
     * Creates a file in the Osiris/ directory
     *
     * @param fileName name of the file to create
     */
    public static void createFile(final String fileName) {
        File file = new File(FILE_DIRECTORY + fileName + FILE_EXTENSION);

        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("Created file: " + fileName);
                } else {
                    System.out.println("Failed to create file: " + fileName);
                }
            } catch (Exception e) {
                System.out.println("Failed to create file: " + fileName + " - " + e.getMessage());
            }
        }
    }

    /**
     * Deletes a file from the Osiris/ directory
     *
     * @param fileName name of the file to delete
     */
    public static void deleteFile(final String fileName) {
        File file = new File(FILE_DIRECTORY + fileName + FILE_EXTENSION);

        if (file.exists()) {
            try {
                if (file.delete()) {
                    System.out.println("Deleted file: " + fileName);
                } else {
                    System.out.println("Failed to delete file: " + fileName);
                }
            } catch (Exception e) {
                System.out.println("Failed to delete file: " + fileName + " - " + e.getMessage());
            }
        }
    }

    /**
     * Reads a file from the Osiris/ directory
     *
     * @param fileName name of the file to read
     * @return file content as a string
     */
    public static String readFile(final String fileName) {
        File file = new File(FILE_DIRECTORY + fileName + FILE_EXTENSION);

        if (file.exists()) {
            try {
                return new String(Files.readAllBytes(file.toPath()));
            } catch (Exception e) {
                System.out.println("Failed to read file: " + fileName + " - " + e.getMessage());
            }
        }

        return null;
    }

    /**
     * Writes to a file in the Osiris/ directory
     *
     * @param fileName name of the file to write to
     * @param content  content to write to the file
     */
    public static void writeFile(final String fileName, final String content) {
        File file = new File(FILE_DIRECTORY + fileName + FILE_EXTENSION);

        if (!file.exists()) {
            createFile(fileName);
        }

        try {
            Files.write(file.toPath(), content.getBytes());
        } catch (Exception e) {
            System.out.println("Failed to write to file: " + fileName + " - " + e.getMessage());
        }
    }
}
