package mathquiz.objs;

//**********************************************************/
// Filename: MathQuiz.java
// Purpose:  To create an object to store math quiz
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqa-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class MathQuiz{
    
    protected double fNum;
    protected double sNum;
    protected double answer;
    protected String operator;
    
    //Construction
    public MathQuiz(String fNum, String operator, String sNum){
        
        this.fNum = Double.parseDouble(fNum);
        this.sNum = Double.parseDouble(sNum);
        this.operator = operator;
        this.answer = this.autoCalculate();
        
        //Check if the answer is infinity or NaN then assign 0 for it
        if (this.answer == Double.POSITIVE_INFINITY | this.answer == Double.NEGATIVE_INFINITY |
                 Double.isNaN(answer)){
            
            this.answer = 0;
        }
    }
    
    //**********************************************************/
    // Mothod:  public String getAnswer()
    // Purpose: To set answer
    // Input:   None
    // Output:  String
    //**********************************************************/
    public String getAnswer(){
        
        //Check answer is integer
        if (answer % 1 == 0){
            return String.valueOf(Math.round(answer));
        }
        //If answer is not integer, but's a decimal
        else
            if (String.valueOf(answer).substring(String.valueOf(answer).length() - 2, String.valueOf(answer).length() - 1).equals("."))
                return String.format("%.1f", answer);
            
            else
                return String.format("%.2f", answer);
    }
    
    //**********************************************************/
    // Mothod:  private double autoCalculate()
    // Purpose: To calculate the inputted math quiz depanding on
    //          +, - ,÷ and x
    // Input:   None
    // Output:  double
    //**********************************************************/
    private double autoCalculate(){       
        
        switch (operator) {
            case "+":
                answer = this.fNum + this.sNum;
                break;
            case "-":
                answer = this.fNum - this.sNum;
                break;
            case "x":
                answer = this.fNum * this.sNum;
                break;
            case "÷":
                answer = this.fNum / this.sNum;
                break;
            default:
                break;
        }     
        return answer;
    }
    
    //**********************************************************/
    // Mothod:  public String toString()
    // Purpose: To Override toString as an output format of this
    //          object
    // Input:   None
    // Output:  String
    //**********************************************************/
    @Override
    public String toString(){      
        
        //Check question format in decimal & int & int. e.g. 1 + 1 = 2
        if (fNum % 1 != 0 & sNum % 1 == 0 & answer % 1 == 0){
            return fNum + " " + operator + " " + Math.round(sNum) + " = " + Math.round(answer); 
        }
        
        //Check question format in int & decimal & int. e.g. 2 x 0.5 = 1
        else if (fNum % 1 == 0 & sNum % 1 != 0 & answer % 1 == 0){
            return Math.round(fNum) + " " + operator + " " + sNum + " = " + Math.round(answer); 
        }
        
        //Check question format in int & int & decimal. e.g. 1 ÷ 2 = 0.5
        else if (fNum % 1 == 0 & sNum % 1 == 0 & answer % 1 != 0)
        {
            if (String.valueOf(answer).substring(String.valueOf(answer).length() - 2, String.valueOf(answer).length() - 1).equals("."))
                return Math.round(fNum) + " " + operator + " " + Math.round(sNum) + " = " + String.format("%.1f",answer); 

            else
                return Math.round(fNum) + " " + operator + " " + Math.round(sNum) + " = " + String.format("%.2f",answer); 
        }
        
        //Check question format in decimal & decimal & int. e.g. 0.5 + 0.5 = 1
        else if (fNum % 1 != 0 & sNum % 1 != 0 & answer % 1 == 0)
        {
            return fNum + " " + operator + " " + sNum + " = " + Math.round(answer);
        }
        
        //Check question format in int & decimal & decimal. e.g. 1 + 0.5 = 1.5
        else if (fNum % 1 == 0 & sNum % 1 != 0 & answer % 1 != 0)
        {
            if (String.valueOf(answer).substring(String.valueOf(answer).length() - 2, String.valueOf(answer).length() - 1).equals("."))
                return Math.round(fNum) + " " + operator + " " + sNum + " = " + String.format("%.1f",answer);
            else
                return Math.round(fNum) + " " + operator + " " + sNum + " = " + String.format("%.2f",answer);
        }
        
        //Check question format in decimal & int & decimal. e.g. 0.5 + 1 = 1.5
        else if (fNum % 1 != 0 & sNum % 1 == 0 & answer % 1 != 0)
        {  
            if (String.valueOf(answer).substring(String.valueOf(answer).length() - 2, String.valueOf(answer).length() - 1).equals("."))
                return fNum + " " + operator + " " + Math.round(sNum) + " = " + String.format("%.1f",answer);
            else
                return fNum + " " + operator + " " + Math.round(sNum) + " = " + String.format("%.2f",answer);
        }
        
        //Check question format in decimal & decimal & decimal. e.g. 0.2 + 0.3 = 0.5
        else if (fNum % 1 != 0 & sNum % 1 != 0 & answer % 1 != 0)
        {
            if (String.valueOf(answer).substring(String.valueOf(answer).length() - 2, String.valueOf(answer).length() - 1).equals("."))
                return fNum + " " + operator + " " + sNum + " = " + String.format("%.1f", answer);
            else
                return fNum + " " + operator + " " + sNum + " = " + String.format("%.2f",answer);
        }

        else
            return Math.round(fNum) + " " + operator + " " + Math.round(sNum) + " = " + Math.round(answer);       
    }
    
}
