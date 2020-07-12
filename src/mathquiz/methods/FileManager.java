package mathquiz.methods;

//Import IO package
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//**********************************************************/
// Filename: FileManager.java
// Purpose:  To give functionalities that save data to an 
//           external file with a specific directory
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqc-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class FileManager {
    
    private static boolean isWritten = false;
    
    //**********************************************************/
    // Mothod:  public static boolean writeToFile(String data, 
    //                                            String path)
    // Purpose: To write data into a file and save it to a path
    //          given
    // Input:   String, String
    // Output:  boolean
    //**********************************************************/
    public static boolean writeToFile(String data, String path){
        
        File f = new File(path);
        
        //Check if the file to save has already existed
        if (!f.exists()){
            
            //Try create the directory with the path given and the file to save
            try {               
                File fPath = new File(f.getParent());
                
                //Check if the given path has already existed
                if (! fPath.exists()){
                    //If there is no then make a directory
                    fPath.mkdir();
                }
                
                //Create the file 
                f.createNewFile();
            }
            catch (IOException ioe){
                System.out.println("Excepetion Occured: " + ioe.toString());
            }
        }
        
        //Try write data into the file
        try (FileWriter fw = new FileWriter(f.getAbsoluteFile(), true);
             BufferedWriter bw = new BufferedWriter(fw)){
            
            bw.write(data);
            bw.close();
            fw.close();  
            isWritten = true;   
        }
        catch (IOException ioe)
        {
            System.out.println("Error occured while saving data to file " + ioe.toString());
        }
        
        return isWritten;
    }
    
    //**********************************************************/
    // Mothod:  public static void emptyOutFile(String path)
    // Purpose: To empty out a file 
    // Input:   String
    // Output:  void
    //**********************************************************/
    public static void emptyOutFile(String path){
        
        File f = new File(path);
        
        //Try write nothing to the file
        try (FileWriter fw = new FileWriter(f.getAbsoluteFile(), false); 
             PrintWriter pw = new PrintWriter(fw, false)){
            
            pw.flush();
            pw.close();
            fw.close();
        }
        catch (IOException ioe){
            System.out.println("Error occured while cleaning data to file " + ioe.toString());
        }
    }
}
