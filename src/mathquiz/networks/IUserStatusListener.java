package mathquiz.networks;

//**********************************************************/
// Filename: IUserStatusListener.java
// Purpose:  To give abstract method that track user is 
//           online or offline
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqser-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public interface IUserStatusListener {
    public void online(String login);
    public void offline(String login);
}
