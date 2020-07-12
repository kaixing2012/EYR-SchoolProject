package mathquiz.dbc;

//Import SQL library
import java.sql.SQLException;

//**********************************************************/
// Filename: DBClient.java
// Purpose:  To give an API for the DBClientManager
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqc-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class DBClient {

    //Declare variable for DBClientManager
    private final DBClientManager manager;
    
    //Constructor
    public DBClient(String url, String usr, String pwd){ 
        manager = new DBClientManager(url, usr, pwd);
    }
    
    //**********************************************************/
    // Mothod:  public void setDataCollector(IDataCollector declarer)
    // Purpose: To give an API to set up the data collector to 
    //          DataClientManager
    // Input:   IDataCollector
    // Output:  void
    //**********************************************************/
    public void setDataCollector(IDataCollector declarer){
        manager.setDataCollector(declarer);   
    }
    
    //**********************************************************/
    // Mothod:  public void setQueryType(String type)
    // Purpose: To give an API to set up the query type to 
    //          DataClientManager
    // Input:   String
    // Output:  void
    //**********************************************************/
    public void setQueryType(String type) {
        manager.setQueryType(type);
    }
    
    //**********************************************************/
    // Mothod:  public void query(String sql) throws 
    //          SQLException, Exception
    // Purpose: To give an API to DBClientManager and retrieve 
    //          data from the database
    // Input:   String
    // Output:  void
    //**********************************************************/
    public void query(String sql) throws SQLException, Exception{
        try {
            //Format the token in this case the token for query
            String query = "query null " + sql;
            manager.run(query);
        }
        catch (SQLException e){
            throw e;
        }

        catch (Exception e){
            throw e;
        } 
    } 
    
    //**********************************************************/
    // Mothod:  public boolean manipulate(Object object,  
    //          String sql) throws SQLException, Exception
    // Purpose: To give an API to DBClientManager and manipulate 
    //          data to the database
    // Input:   Object, String
    // Output:  boolean
    //**********************************************************/
    public boolean manipulate(Object object, String sql)throws SQLException, Exception{       
        try {
            //Format the token in this case the token for manipulation
            //The inputted object can be any customised object but
            //needs to override toString method with CSV format
            String manipulation = "manipulation " + object + " " + sql;
            manager.run(manipulation);
        }
        catch (Exception e) {
            throw e;
        }
        
        return manager.isManipulated();
    }
}
