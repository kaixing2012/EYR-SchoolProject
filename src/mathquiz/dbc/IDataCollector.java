package mathquiz.dbc;

import java.sql.ResultSet;

//**********************************************************/
// Filename: IDataCollector.java
// Purpose:  To give abstract method to DBClientManager which
//           create database connectivity
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqc-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public interface IDataCollector {
    
    //**********************************************************/
    // Mothod:  public boolean retrieve(ResultSet rs, 
    //                                  String queryType)
    // Purpose: To give an abstract method for retrieving data in
    //          DBClientManager
    // Input:   ResultSet, String
    // Output:  boolean
    //**********************************************************/
    public boolean retrieve(ResultSet rs, String queryType);
}
