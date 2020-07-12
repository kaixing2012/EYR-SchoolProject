package mathquiz.networks;

//**********************************************************/
// Filename: IUserStatusListener.java
// Purpose:  To give abstract method that listen message sent
//           from the other side
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqser-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public interface IMessageListener {
    public void onMessage(String fromMessage, String msgBody);
    
}
