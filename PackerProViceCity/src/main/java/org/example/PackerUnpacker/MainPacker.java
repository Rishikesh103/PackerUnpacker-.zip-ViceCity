package org.example.PackerUnpacker;
import org.example.PackerUnpacker.Packer;

import java.io.*;

public class MainPacker
{
    private String PackName;
    private String DirName;

    public MainPacker(String A, String B)
    {
        this.PackName = A;
        this.DirName = B;
    }

    public void PacckingActivity()
    {
        try
        {
            System.out.println("-----------------------------------------------");
            System.out.println("------------Marvellous Packer Unpacker------------");
            System.out.println("-----------------------------------------------");
            System.out.println("------------ Packing Activity --------------");
            System.out.println("-----------------------------------------------");

            int i = 0, j = 0, iRet = 0, iCountFile = 0;

            File fobj = new File(DirName);


            // check the exixtance of directory
            if(fobj.exists() && fobj.isDirectory())
            {
                System.out.println(DirName + "is successfully opened");
                File Packobj = new File(PackName);

                // create a packed file
                boolean bRet = Packobj.createNewFile();

                if(bRet == false)
                {
                    System.out.println("Unable to create Pack File");
                    return;
                }

                System.out.println("packed file get successfully get created with :"+PackName);

                // Retrive all files from directory
                File Arr[] = fobj.listFiles();

                // packed file object
                FileOutputStream foobj = new FileOutputStream(Packobj);

                // Buffer for read and write activity
                byte Buffer[] = new byte[1024];

                String Header = null;

                // Dictory Travelsal
                for(i = 0; i < Arr.length; i++)
                {
                    Header = Arr[i].getName() + " " + Arr[i].length();

                    // loop to form 100 bytes header
                    for(j = Header.length(); j < 100; j++)
                    {
                        Header = Header+" ";
                    }

                    // write header into packed file
                    foobj.write(Header.getBytes());

                    // open file from directory for reading
                    FileInputStream fiobj = new FileInputStream(Arr[i]);

                    // write contents of file into packed file
                    while((iRet = fiobj.read(Buffer)) != -1)
                    {
                        foobj.write(Buffer,0,iRet);
                        System.out.println("FILE size scanned is : "+Arr[i].getName());
                        System.out.println("FILE size read is : "+iRet);
                    }
                    fiobj.close();
                    iCountFile++;
                }
                System.out.println("Packing activity done");

                System.out.println("-----------------------------------------------");
                System.out.println("---------------Statistical Report--------------");
                System.out.println("-----------------------------------------------");

                // to be added

                System.out.println("Total file packed :"+iCountFile);

                System.out.println("-----------------------------------------------");
                System.out.println("-----------Thankyou for using our app------------");
                System.out.println("-----------------------------------------------");
            }
            else
            {
                System.out.println("There is no such Directory");
            }
        }// End of try
        catch(Exception eobj)
        {

        }
    }// End of PackingActiviy function
}
// End of MarvellousPacker class
