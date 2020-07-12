package mathquiz.dbc;

//Import SQL packages
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//Import third party libraries
import org.apache.commons.lang3.StringUtils;

//**********************************************************/
// Filename: DBClientManager.java
// Purpose:  To create database connectivity
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqd-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class DBClientManager {

    //Declare variables for database login
    private final String url;
    private final String usr;
    private final String pwd;
    
    //Declare variables for database connectivity
    private Connection conn;
    private Statement stmt;
    private PreparedStatement pstmt;
    private ResultSet rs;
    private ResultSetMetaData rsmd;
    
    //Declare variables for return
    private int recordNum;
    private int columnNum;
    
    private String queryType;
    private String errorMessage;
    
    //Declare variables for boolean computing
    private boolean isRetrieved = false;
    private boolean isManipulated = false;
    
    //Declare interface variables for database
    private IDataCollector collector;
    
    //Constructor
    public DBClientManager(String url, String usr, String pwd){
        this.url = url;
        this.usr = usr;
        this.pwd = pwd;
    }
    
    //**********************************************************/
    // Mothod:  public boolean isRetrieved()
    // Purpose: To return true or false due to the result
    // Input:   none
    // Output:  boolean
    //**********************************************************/
    public boolean isRetrieved(){
        return isRetrieved;
    }
    
    //**********************************************************/
    // Mothod:  public boolean isManipulated()
    // Purpose: To return true or false due to the result
    // Input:   none
    // Output:  boolean
    //**********************************************************/
    public boolean isManipulated(){
        return isManipulated;
    }
    
    //**********************************************************/
    // Mothod:  public void setDataCollector(IDataCollector collector)
    // Purpose: To set the IDataCollector interface and therefore,
    //          retrieve the abstract method can be used in this class
    // Input:   IDataCollector
    // Output:  boolean
    //**********************************************************/
    public void setDataCollector(IDataCollector collector) {
        this.collector = collector;
    }
    
    //**********************************************************/
    // Mothod:  public void setQueryType(String type)
    // Purpose: To set a query type that guild system to where 
    //          the data is to be retrieved 
    // Input:   none
    // Output:  void
    //**********************************************************/
    public void setQueryType(String type) {
        this.queryType = type;
    }
    
    //**********************************************************/
    // Mothod:  public int getRecordCounts()
    // Purpose: To get record counts
    // Input:   none
    // Output:  int
    //**********************************************************/
    public int getRecordCounts(){
        return recordNum;
    }
    
    //**********************************************************/
    // Mothod:  public int getColumnCounts()
    // Purpose: To get column counts
    // Input:   none
    // Output:  int
    //**********************************************************/
    public int getColumnCounts(){
        return columnNum;
    }
    
    //**********************************************************/
    // Mothod:  public String getErrorMessage()
    // Purpose: To get error message from SQL
    // Input:   none
    // Output:  String
    //**********************************************************/
    public String getErrorMessage(){
        return errorMessage;
    }
    
    //**********************************************************/
    // Mothod:  public void run(String token) throws 
    //          SQLException, Exception
    // Purpose: To run manager. Parameter token will be written
    //          like "cmd values sql" and therefore the cmd will
    //          tell the system which private method in this class
    //          will be run. Values will used for manipulation of data
    //          sys
    // Input:   String
    // Output:  void
    //**********************************************************/
    public void run(String token) throws SQLException, Exception{
        
        //Check if token is not null
        if(token.length() != 0){
            
            //Solit token into 3 parts[cmd, values, sql]
            String[] tokens = StringUtils.split(token, null, 3);
            
            if(tokens != null && tokens.length > 0){
                
                //Assigh first index of the tokens to cmd variable
                String cmd = tokens[0];
                
                //Check check cmd type
                if("query".equalsIgnoreCase(cmd)){
                    //run manageQuery if cmd is query
                    manageQuery(tokens);
                }
                else if ("manipulation".equalsIgnoreCase(cmd)){
                    //run manageManipulation if cmd is manipulation
                    manageManipulation(tokens);
                }
            }
        }
    }
    
    //**********************************************************/
    // Mothod:  private void manageQuery(String[] tokens) throws 
    //          SQLException, Exception
    // Purpose: To retrieve data from the database
    // Input:   String
    // Output:  void
    //**********************************************************/
    private void manageQuery(String[] tokens) throws SQLException, Exception{
        
        //Check if tokens are three
        if(tokens.length == 3){
            
            String sql = tokens[2];
            
            //Try database connection
            try{
                conn = DriverManager.getConnection(url, usr, pwd);
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                rsmd = rs.getMetaData();

                //Check if reault set is not null
                if (rs != null){
                    
                    //Get record counts of data and column numbers of data 
                    rs.beforeFirst();
                    rs.last();
                    recordNum = rs.getRow();
                    columnNum = rsmd.getColumnCount();
                    
                    //If record counts is greater than 0
                    if (recordNum > 0){
                        //Set record position at the beginning
                        rs.beforeFirst();

                        //While result set has next then run retrieve mwthod 
                        //provided by the IDataCollector interface
                        while (rs.next()){
                            //Check if the data is retrieved
                            if(collector.retrieve(rs, queryType)){
                                isRetrieved = true;
                            }
                        }
                    }
                }
            }
            catch (SQLException e){
                errorMessage = e.getMessage();
                throw e;
            }
            catch (Exception e){
                errorMessage = e.getMessage();
                throw e;
            } 
            //Close database connections
            finally{         
                if(rs != null){
                    rs.close();
                    rsmd = null;
                    rs = null;
                    System.out.println("rs closed");
                }

                if(stmt != null){
                    stmt.close();
                    stmt = null;
                    System.out.println("stmt closed");
                }
                
                if(conn != null){
                    conn.close();
                    conn = null;
                    System.out.println("conn closed");
                }
            }
        }
    }

    //**********************************************************/
    // Mothod:  private void manageManipulation(String[] tokens) throws 
    //          SQLException, Exception
    // Purpose: To manipulate data to the database
    // Input:   String
    // Output:  void
    //**********************************************************/
    private void manageManipulation(String[] tokens) throws SQLException {
        
        //Check if tokens are three
        if(tokens.length == 3){
            
            //Assign variables to second and third index of tokens array
            String[] values = tokens[1].split(",");
            String sql = tokens[2];
            
            //Try database connectivity
            try{
                conn = DriverManager.getConnection(url, usr, pwd);
                pstmt = conn.prepareStatement(sql);
                
                //Manipulate data to the databse with a loop
                for (int i = 0; i < values.length; i++){
                    pstmt.setObject(i + 1, values[i]);
                }

                //Execute manipulation
                pstmt.executeUpdate();
                isManipulated = true;
            }
            catch (SQLException e){   
                errorMessage = e.getMessage();
                throw e;
            }
            catch (Exception e){
                errorMessage = e.getMessage();
                throw e;
            }
            //Close the database connection 
            finally{
                if(pstmt != null){
                    pstmt.close();
                    pstmt = null;
                    System.out.println("pstmt closed");
                }
                
                if(conn != null){
                    conn.close();
                    conn = null;
                    System.out.println("conn closed");
                }
            }
        }
    }  
}
