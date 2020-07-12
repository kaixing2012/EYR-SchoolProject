package mathquiz.objs;

//**********************************************************/
// Filename: SortByOperator.java
// Purpose:  To inherit properties from MathQuiz and implements
//           the interface of Comparable
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqbo-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class SortByOperator extends MathQuiz implements Comparable <SortByOperator>{
    
    public SortByOperator(String fNum, String operator, String sNum){
            super(fNum, operator, sNum);
        }
        
    //**********************************************************/
    // Mothod:  public int compareTo(SortByOperator other)
    // Purpose: To Override compareTo method that compare the
    //          MathQuiz object by its operator
    // Input:   SortByOperator
    // Output:  int
    //**********************************************************/
    @Override
    public int compareTo(SortByOperator other) {
        return this.operator.compareTo(other.operator);
    }
}
