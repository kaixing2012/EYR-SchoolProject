package mathquiz.objs;

//**********************************************************/
// Filename: User.java
// Purpose:  To create an object to store user's info
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqu-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class User {

    private final String f;
    private final String l;
    private final String role;
    private int user_id;
    
    public User(String fName, String lName, String role){
        this.f = fName;
        this.l = lName;
        this.role = role;
    }
    
    //**********************************************************/
    // Mothod:  public int getUserID()
    // Purpose: To get user id
    // Input:   None
    // Output:  int
    //**********************************************************/
    public int getUserID(){
        return this.user_id;
    }
    
    //**********************************************************/
    // Mothod:  public String getFirstName()
    // Purpose: To get user's first name
    // Input:   None
    // Output:  String
    //**********************************************************/
    public String getFirstName(){
        return this.f;
    }
    
    //**********************************************************/
    // Mothod:  public String getSecondName()
    // Purpose: To get user's second name
    // Input:   None
    // Output:  String
    //**********************************************************/
    public String getSecondName(){
        return this.l;
    }
    
    //**********************************************************/
    // Mothod:  public String getRole()
    // Purpose: To get user's role
    // Input:   None
    // Output:  String
    //**********************************************************/
    public String getRole(){
        return this.role;
    }
    
    //**********************************************************/
    // Mothod:  public void setUserID(int user_id)
    // Purpose: To set user's id
    // Input:   int
    // Output:  void
    //**********************************************************/
    public void setUserID(int user_id){
        this.user_id = user_id;
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
        return this.f + "," + this.l + "," + this.role;
    }
}
