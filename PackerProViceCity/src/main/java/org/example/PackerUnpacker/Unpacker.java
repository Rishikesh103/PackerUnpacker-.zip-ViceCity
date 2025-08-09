package org.example.PackerUnpacker;

import java.util.Scanner;

public class Unpacker {
    public static void main(String[] args) {
        try {
            Scanner sobj = new Scanner(System.in);

            System.out.println("Enter the name of the file which contains packed data:");
            String packName = sobj.nextLine();

            System.out.println("Enter the destination directory to unpack files into:");
            String destDir = sobj.nextLine();

            // Use the updated MainUnpacker constructor with two arguments
            MainUnpacker mobj = new MainUnpacker(packName, destDir);
            mobj.UnpackingActivity();

            sobj.close();
        } catch (Exception eobj) {
            System.out.println("An error occurred in the Unpacker application.");
            eobj.printStackTrace();
        }
    }
}
