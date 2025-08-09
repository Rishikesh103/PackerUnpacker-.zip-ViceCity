package org.example.PackerUnpacker;
import org.example.PackerUnpacker.MainPacker;

import java.util.*;


public class Packer {
    public static void main(String A[]) {
        try {
            Scanner sobj = new Scanner(System.in);

            System.out.println("Enter the name of Directory that you want to pack:");
            String DirName = sobj.nextLine();

            System.out.println("Enter the name of file that you want to create for packing:");
            String PackName = sobj.nextLine();

            MainPacker mobj = new MainPacker(PackName, DirName);

            mobj.PacckingActivity();
        } catch (Exception eobj) {

        }
    }
}
