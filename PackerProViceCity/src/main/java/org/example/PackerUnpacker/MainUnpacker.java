package org.example.PackerUnpacker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainUnpacker {
    private final String packName;
    private final String destDir;

    /**
     * Constructor updated to accept the destination directory.
     * @param packName The full path of the file to unpack.
     * @param destDir The full path of the directory to save the unpacked files.
     */
    public MainUnpacker(String packName, String destDir) {
        this.packName = packName;
        this.destDir = destDir;
    }

    public void UnpackingActivity() {
        try {
            System.out.println("------------ Unpacking Activity --------------");
            File packFile = new File(packName);

            if (!packFile.exists()) {
                System.out.println("Error: The packed file does not exist.");
                return;
            }

            FileInputStream fis = new FileInputStream(packFile);
            byte[] headerBuffer = new byte[100];
            int bytesRead;
            int fileCount = 0;

            // Create destination directory if it doesn't exist
            File destination = new File(destDir);
            if (!destination.exists()) {
                destination.mkdirs();
            }

            while ((bytesRead = fis.read(headerBuffer, 0, 100)) > 0) {
                String header = new String(headerBuffer, 0, bytesRead).trim();
                String[] tokens = header.split(" ");

                if (tokens.length < 2) continue;

                String fileName = tokens[0];
                int fileSize;
                try {
                    fileSize = Integer.parseInt(tokens[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid file size in header for " + fileName + ". Skipping.");
                    continue;
                }

                // Create the new file inside the specified destination directory
                File newFile = new File(destDir, fileName);
                FileOutputStream fos = new FileOutputStream(newFile);

                byte[] fileBuffer = new byte[fileSize];
                fis.read(fileBuffer, 0, fileSize);
                fos.write(fileBuffer, 0, fileSize);

                fos.close();
                System.out.println("Unpacked: " + newFile.getAbsolutePath());
                fileCount++;
            }

            fis.close();
            System.out.println("------------ Statistical Report --------------");
            System.out.println("Total files unpacked: " + fileCount);
            System.out.println("Unpacking activity completed successfully.");

        } catch (Exception e) {
            System.out.println("An error occurred during unpacking.");
            e.printStackTrace();
        }
    }
}
