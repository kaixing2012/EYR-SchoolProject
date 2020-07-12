import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import mathquiz.objs.MathQuiz;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class JUTestMathCalculator {
    
    private MathQuiz mathQuiz;
    
    private final String firstNum;
    private final String operator;
    private final String secondNum;
    private final String expectedAnswer;
      
    public JUTestMathCalculator(String firstNum, String operator, 
                                String secondNum, String expectedAnswer) {
        
        this.firstNum = firstNum;
        this.operator = operator;
        this.secondNum = secondNum;
        this.expectedAnswer = expectedAnswer;
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
        mathQuiz = new MathQuiz(firstNum, operator, secondNum);
        System.out.println("Math question (" + firstNum + " " 
                         + operator + " " + secondNum + " = ) is created.\n");
    }
    
    @After
    public void tearDown() {
        System.out.println("Unit test is finished.\n");
        System.out.println("-----------------------------------------------\n");
    }
    
    @Parameters
    public static Collection<Object[]> data() { 
        return Arrays.asList(new Object[][] {
            {"1", "÷", "0", "0"},
            {"1.1", "+", "1.1", "2.2"},
            {"1", "÷", "2", "0.5"}
        });
    }
    @Test
     public void serverConnectionTest() {    
        System.out.println("Start to test if expected answer " + expectedAnswer 
                         + " is equal to\nthe answer from function in MathQuiz\n");

        assertEquals(expectedAnswer, mathQuiz.getAnswer());
     }
}
