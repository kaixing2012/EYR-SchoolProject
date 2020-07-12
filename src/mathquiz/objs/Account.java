package mathquiz.objs;

//**********************************************************/
// Filename: Account.java
// Purpose:  To create an object to store user account info
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqa-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class Account {

    private final String name;
    private final String pwd;
    private int usr_id;
    
    //Construction
    public Account(String username, String password) {
        this.name = username;
        this.pwd = password;
        this.usr_id = 0;
    }
    
    //**********************************************************/
    // Mothod:  public void setUserID(int user_id)
    // Purpose: To set user id
    // Input:   int
    // Output:  void
    //**********************************************************/
    public void setUserID(int user_id){
        this.usr_id = user_id;
    }

    //**********************************************************/
    // Mothod:  public String getUsername()
    // Purpose: To get user name
    // Input:   None
    // Output:  String
    //**********************************************************/
    public String getUsername(){
        return this.name;
    }
    
    //**********************************************************/
    // Mothod:  public String getPassword()
    // Purpose: To get password
    // Input:   None
    // Output:  String
    //**********************************************************/
    public String getPassword(){
        return this.pwd;
    }
    
    //**********************************************************/
    // Mothod:  public int getUserID()
    // Purpose: To get user id
    // Input:   None
    // Output:  int
    //**********************************************************/
    public int getUserID(){
        return this.usr_id;
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
        return this.name + "," + this.pwd + "," + this.usr_id;
    }
}
