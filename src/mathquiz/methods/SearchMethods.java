package mathquiz.methods;

//Import Util
import java.util.Collections;
import java.util.List;

//**********************************************************/
// Filename: SearchMethods.java
// Purpose:  To give functionalities that help to search 
//           if a type of data is in a data structure
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqs-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class SearchMethods {
    
    private static boolean searchStatus = false;
    
    //**********************************************************/
    // Mothod:  public static <T extends Comparable<T>> boolean
    //          linearSearch(T dataToSearch, T[] array)
    // Purpose: To provide a linear way to search for data
    // Input:   T, T[]
    // Output:  <T extends Comparable<T>> boolean
    //**********************************************************/
    public static <T extends Comparable<T>> boolean linearSearch(T dataToSearch, T[] array){
        
        //Use the for loop to linearly search for data
        for (int i = 0; i < array.length; i++)
        {
            if (dataToSearch.equals(array[i]))
            {
                searchStatus = true;
                break;
            }
        }

        return searchStatus;
    }
    
    //**********************************************************/
    // Mothod:  public static <T extends Comparable<T>> boolean
    //          binarySearch(T dataToSearch, T[] array)
    // Purpose: To provide a binary way to search for data
    // Input:   List<T>, T
    // Output:  <T extends Comparable<T>> boolean
    //**********************************************************/
    public static <T extends Comparable<T>> boolean binarySearch(List<T> list, T dataToSearch){
   
        //Sort the list
        Collections.sort(list);
        
        int low = 0;
        int high = list.size() - 1;
        
        //While low point is less and equal to high
        while (low <= high){
            
            //Pivot a point on the middle list
            int mid = (low + high) / 2;
            
            //Swap elements if meets the condition
            if (list.get(mid).compareTo(dataToSearch) < 0){
                low = mid + 1;
            }
            else if (list.get(mid).compareTo(dataToSearch) > 0){
                high = mid - 1;
            }
            else if (list.get(mid).compareTo(dataToSearch) == 0){
                searchStatus = true;
                break;
            }
        }   
        return searchStatus;
    }
}
