import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class FileComparer
{
    public static void main(String[] args)
    {
        try
        {
            BufferedInputStream inFromFile0 = new BufferedInputStream(new FileInputStream(args[0]));
            BufferedInputStream inFromFile1 = new BufferedInputStream(new FileInputStream(args[1]));
            
            byte[] file0read = new byte[256];
            byte[] file1read = new byte[256];
            
            System.out.println("Comparing...");
            
            while (inFromFile0.available() > 0 || inFromFile1.available() > 0)
            {
                inFromFile0.read(file0read);
                inFromFile1.read(file1read);
                
                if ((new String(file0read)).compareTo(new String(file1read)) != 0)
                {
                    System.out.println("Files not equal!!!");
                }
            }
            
            System.out.println("Done.");
        }
        catch (Exception e)
        {}
    }
}
