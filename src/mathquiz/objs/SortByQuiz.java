package mathquiz.objs;

//**********************************************************/
// Filename: SortByQuiz.java
// Purpose:  To inherit properties from MathQuiz and implements
//           the interface of Comparable
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqbq-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class SortByQuiz extends MathQuiz implements Comparable<SortByQuiz> {
    
    public SortByQuiz (String fNum, String operator, String sNum){
            super(fNum, operator, sNum);
        }

    //**********************************************************/
    // Mothod:  public int compareTo(SortByQuiz other)
    // Purpose: To Override compareTo method that compare the
    //          MathQuiz object by the whole quiz itself
    // Input:   SortByQuiz
    // Output:  int
    //**********************************************************/
    @Override
    public int compareTo(SortByQuiz other) {
        return this.toString().compareTo(other.toString());
    }
}
