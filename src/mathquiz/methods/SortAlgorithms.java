package mathquiz.methods;

//Import Util package
import java.util.ArrayList;
import java.util.List;

//**********************************************************/
// Filename: SortAlgorithms.java
// Purpose:  To give functionalities that can sort a data
//           structure
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqsa-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class SortAlgorithms {
    
    //**********************************************************/
    // Mothod:  public static <T extends Comparable<T>> boolean
    //          insertionSortAsc(List<T> list)
    // Purpose: To provide a public method for sorting 
    //          data structure in an ascending way with
    //          insertion sort
    // Input:   List<T>
    // Output:  <T extends Comparable<T>> void
    //**********************************************************/
    public static <T extends Comparable<T>> void insertionSortAsc(List<T> list){
        insertionSortCore(list, "asc");
    }
    
    //**********************************************************/
    // Mothod:  public static <T extends Comparable<T>> boolean
    //          insertionSortDesc(List<T> list)
    // Purpose: To provide a public method for sorting 
    //          data structure in an descending way with
    //          insertion sort
    // Input:   List<T>
    // Output:  <T extends Comparable<T>> void
    //**********************************************************/
    public static <T extends Comparable<T>> void insertionSortDesc(List<T> list){
        insertionSortCore(list, "desc");
    }
    
    //**********************************************************/
    // Mothod:  private static <T extends Comparable<T>> boolean
    //          insertionSortCore(List<T> list, String order)
    // Purpose: To provide the core function for sorting 
    //          data structure in an descending way with
    //          insertion sort
    // Input:   List<T>, String
    // Output:  <T extends Comparable<T>> void
    //**********************************************************/
    private static <T extends Comparable<T>> void insertionSortCore(List<T> list, String order){
        
        //Run a for loop in a linear way to sort the data structure
        for (int i = 1; i < list.size(); i++){          
            for (int j = i; j > 0; j--){
                
                //Check for order varieble that is equal to ascending
                if ("asc".equalsIgnoreCase(order)){
                    if (list.get(j).compareTo(list.get(j - 1)) < 0){                        
                        T temp = list.get(j);
                        list.set(j, list.get(j - 1));
                        list.set(j - 1, temp);
                    }
                }
                //Check for order varieble that is equal to descending
                else if ("desc".equalsIgnoreCase(order)){
                    if (list.get(j).compareTo(list.get(j - 1)) > 0){
                        T temp = list.get(j);
                        list.set(j, list.get(j - 1));
                        list.set(j - 1, temp);
                    }
                }
            }
        }
    }
    
    //**********************************************************/
    // Mothod:  public static <T extends Comparable<T>> boolean
    //          quickSort(List<T> list)
    // Purpose: To provide a public method for sorting 
    //          data structure with quick sort
    // Input:   List<T>
    // Output:  <T extends Comparable<T>> void
    //**********************************************************/
    public static <T extends Comparable<T>> void quickSort (List<T> list){
        quickSortCore(list, 0, list.size() - 1);
    }
    
    //**********************************************************/
    // Mothod:  private static <T extends Comparable<T>> boolean
    //          quickSortCore(List<T> list, int start, int end)
    // Purpose: To provide a core function for sorting 
    //          data structure with quick sort
    // Input:   List<T>, int, int
    // Output:  <T extends Comparable<T>> void
    //**********************************************************/
    private static <T extends Comparable<T>> void quickSortCore(List<T> list, int start, int end){
       
        // start index
        int i = start;
        // end index
        int j = end;

        // pivot is half-way
        T middleValue = list.get((i + j) / 2); 

        //Keep run the process until list sorted
        while (true){
            
            //Keep sorting one part of the data structure
            while (list.get(i).compareTo(middleValue) > 0)
                i++;

            //Keep sorting the other part of the data structure
            while (list.get(j).compareTo(middleValue) < 0)
                j--;

            //Swap elements
            if (i <= j)
            {
                T temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
                i++;
                j--;
            }

            if (i > j)
                break;
        }
        
        //Call the method itself if condition is not net
        if (start < j)
            quickSortCore(list, start, j);

        //Call the method itself if condition is not net
        if (i < end)
            quickSortCore(list, i, end);
    }
    
    //**********************************************************/
    // Mothod:  public static <T extends Comparable<? super T>> List<T>
    //          boolean mergeSort(List<T> list)
    // Purpose: To provide a public method for sorting 
    //          data structure with merge sort
    // Input:   List<T>
    // Output:  <T extends Comparable<? super T>> List<T>
    //**********************************************************/
    public static <T extends Comparable<? super T>> List<T> mergeSort(List<T> list){
        return mergeSortCore(list);
    }
    
    //**********************************************************/
    // Mothod:  private static <T extends Comparable<? super T>> List<T>
    //          boolean mergeSort(List<T> list)
    // Purpose: To provide a core function for sorting 
    //          data structure with merge sort
    // Input:   List<T>
    // Output:  <T extends Comparable<? super T>> List<T>
    //**********************************************************/
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<? super T>> List<T> mergeSortCore(List<T> list)
    {
        List<T> left;
        List<T> right;
        List<T> result =  new ArrayList<>();
        for(int i = 0; i < list.size(); i++)  
            result.add(null);
        
        // base case to avoid an infinite recursion and therefore a stackoverflow
        if (list.size() <= 1)
        {
            return list;
        }

        // The exact midpoint of our list  
        int midPoint = list.size() / 2;
        // left half of the list
        left = new ArrayList<>(midPoint);
        for(int i = 0; i < midPoint; i++)  
            left.add(null);
        
        // if list has an even number of elements, the left and right list 
        // will have the same number of elements
        if (list.size() % 2 == 0)
        {
            right = new ArrayList<>();
            for(int i = 0; i < midPoint; i++)
                right.add(null);
        }
        // if list has an odd number of elements, the right list 
        // will have one more element than the left
        else
        {
            right = new ArrayList<>();
            for(int i = 0; i < midPoint + 1; i++)
                right.add(null);
        }
        // populate left list
        for (int i = 0; i < midPoint; i++)
        {
            left.set(i, list.get(i));
        }    
        // populate right list   
        int x = 0;
        // Start with index from the midpoint for the right list
        // the left list already started from 0 to midpoint
        for (int i = midPoint; i < list.size(); i++)
        {
            right.set(x, list.get(i));
            x++;
        }
        // Recursively sort the left list
        left = mergeSortCore(left);
        // Recursively sort the right list
        right = mergeSortCore(right);
        // Merge the two sorted lists
        result = mergeArrays(left, right);
        return result;
    }
    
    //**********************************************************/
    // Mothod:  private static <T extends Comparable<? super T>> List<T> 
    //          mergeArrays(List<T> left, List<T> right)
    // Purpose: To merge data structure after merge sort function
    // Input:   List<T>, List<T>
    // Output:  <T extends Comparable<? super T>> List<T>
    //**********************************************************/
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<? super T>> List<T> mergeArrays(List<T> left, List<T> right)
    {
        // length of both input lists (total number of elements)
        int resultLength = right.size() + left.size();
        // new list
        List<T> result = new ArrayList<>();
        for(int i = 0; i < resultLength; i++)
            result.add(null);
        // start index values for each of the 3 lists with zero
        int indexLeft = 0, indexRight = 0, indexResult = 0;
        // while either list still has an element
        while (indexLeft < left.size() || indexRight < right.size())
        {
            // if both lists have elements  
            if (indexLeft < left.size() && indexRight < right.size())
            {
                // if item on left list is less than item on right list, 
                // add that item to the result list 
                if (left.get(indexLeft).compareTo(right.get(indexRight)) <= 0)
                {
                    result.set(indexResult, left.get(indexLeft));  
                    indexLeft++;
                    indexResult++;
                }
                // else the item in the right list will be added to the results list
                else
                {
                    result.set(indexResult, right.get(indexRight));  
                    indexRight++;
                    indexResult++;
                }
            }
            // if only the left list still has elements, 
            // add all its items to the results list
            else if (indexLeft < left.size())
            {
                result.set(indexResult, left.get(indexLeft)); 
                indexLeft++;
                indexResult++;
            }
            // if only the right list still has elements, 
            // add all its items to the results list
            else if (indexRight < right.size())
            {
                result.set(indexResult, right.get(indexRight));  
                indexRight++;
                indexResult++;
            }
        }
        return result;
    }  
}
