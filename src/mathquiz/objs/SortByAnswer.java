package mathquiz.objs;

//**********************************************************/
// Filename: SortByAnswer.java
// Purpose:  To inherit properties from MathQuiz and implements
//           the interface of Comparable
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqba-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class SortByAnswer extends MathQuiz implements Comparable<SortByAnswer> {
    
    //Construction
    public SortByAnswer(String fNum, String operator, String sNum){
        super(fNum, operator, sNum);
    }
    
    //**********************************************************/
    // Mothod:  public int compareTo(SortByAnswer other) 
    // Purpose: To Override compareTo method that compare the
    //          MathQuiz object by its answer
    // Input:   SortByAnswer
    // Output:  int
    //**********************************************************/
    @Override
    public int compareTo(SortByAnswer other) {
        return Double.compare(this.answer, other.answer);
    }
    
}
