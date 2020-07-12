package mathquiz.networks;

//Import IO package
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//Import Net package
import java.net.Socket;
import java.net.SocketException;

//Import SQL package
import java.sql.ResultSet;
import java.sql.SQLException;

//Import Util package
import java.util.List;
import java.util.HashSet;

//Import DBC package
import mathquiz.dbc.DBClient;
import mathquiz.dbc.IDataCollector;

//Import Objs package
import mathquiz.objs.Account;

//Import third party library
import org.apache.commons.lang3.StringUtils;

//**********************************************************/
// Filename: ClientManager.java
// Purpose:  To create a thread for server socket and client
//           socket that manager can arrange multiple connection
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqcm-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class ClientManager extends Thread implements IDataCollector{

    //Declare variables for login usage of database connection
    private final String url;
    private final String usr;
    private final String pwd;
    
    //Declare variables for server and client sockets
    private final Socket client;
    private final Server server;
    
    //Declare variables for user role
    private String role;
    
    //Declare variables for database clients
    private DBClient dbClient;
    
    //Declare variables for input and output stream
    private DataOutputStream out;
    private DataInputStream in;
    
    //Declare variables for grouped conversation room
    private final HashSet<String> topicSet = new HashSet<>();
    
    //Declare variables to store user account
    private Account account;

    //Construction
    ClientManager(Server server, Socket clientSocket) {
        this.server = server;
        this.client = clientSocket;
        
        this.url = "jdbc:mysql://localhost/math_quiz?"
            + "useSSL=false&useUnicode=true&"
            + "&useLegacyDatetimeCode=false&"
            + "serverTimezone=UTC";
        this.usr = "root";
        this.pwd = "hiphop17"; 
    }
    
    //**********************************************************/
    // Mothod:  public String getCurrentUser()
    // Purpose: To get user who currently connect
    // Input:   None
    // Output:  String
    //**********************************************************/
    public String getCurrentUser(){
        return account.getUsername();
    }
    
    //**********************************************************/
    // Mothod:  public void run()
    // Purpose: To override the run method of thread class and 
    //          run managers
    // Input:   None
    // Output:  void
    //**********************************************************/
    @Override
    public void run(){
        
        //Try run managers
        try {
            managers();
        }
        catch (IOException ex) {
            System.out.println("ServerRunIOException: " + ex);
        }
    }
    
    //**********************************************************/
    // Mothod:  public void run()
    // Purpose: To override the retrieve method of IDataCollector 
    //          interface that customises functionality to retrieve
    //          data from the database
    // Input:   None
    // Output:  boolean
    //**********************************************************/
    @Override
    public boolean retrieve(ResultSet rs, String queryType) {
        //Try retrieve method
        try{
            //If the query type is accounts than assigh data retrieved into 
            //the account object
            if("accounts".equalsIgnoreCase(queryType)){
                String username = rs.getString("username");
                String password = rs.getString("password");
                int user_id = rs.getInt("user_id");
                
                account = new Account(username, password);
                account.setUserID(user_id);
                
                if(account != null){
                    return true;
                }
            }  
            //Otherwise, retrieve role data of the user from the users table
            else if("users".equalsIgnoreCase(queryType)){
                role = rs.getString("role");
                
                if(role != null){
                    return true;
                }
            }  
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
        return false;
    }

    //**********************************************************/
    // Mothod:  private void managers() throws IOException
    // Purpose: To create managers that follows token given then
    //          run a method required
    // Input:   None
    // Output:  void
    //**********************************************************/
    private void managers() throws IOException{          
        try {
            out = new DataOutputStream(client.getOutputStream());
            in = new DataInputStream(client.getInputStream());
            
            //Keep checking if data stream in is not null
            String line;
            while((line = in.readUTF()) != null){ 
                
                //Split token into tokens array
                String[] tokens = StringUtils.split(line);
                
                if(tokens != null && tokens.length > 0){
                    
                    //Assign cmd variable to first index of tokens array
                    String cmd = tokens[0];
                    
                    //If cmd is logff then run manageLogOut
                    if("logoff".equalsIgnoreCase(cmd)){
                        manageLogOut();
                        break;
                    }
                    //If cmd is login then run manageLogin
                    else if("login".equalsIgnoreCase(cmd)){
                        manageLogin(tokens);
                    }
                    //If cmd is msg then run manageMessage
                    else if("msg".equalsIgnoreCase(cmd)){
                        String[] tokensMsg = StringUtils.split(line, null, 3);
                        manageMessage(tokensMsg);
                    }
                    //If cmd is join then run manageJoin
                    else if("join".equalsIgnoreCase(cmd)){
                        manageJoin(tokens);
                    }
                    //If cmd is leave then run manageLeave
                    else if("leave".equalsIgnoreCase(cmd)){
                        manageLeave(tokens);
                    }
                    //If cmd is unknown then run nothing but sends unknown command
                    else{
                        String msg = "Unknown Command: " + cmd + "\n";
                        out.writeUTF(msg);
                    }
                }
            }
        }
        catch(SocketException se) {
            System.out.println("ManagerSocketException: " + se);
            client.close();
        }
        catch(IOException ioe){
            System.out.println("ManagerIOException: " + ioe);
            client.close();
        }
    } 
    
    //**********************************************************/
    // Mothod:  private void send(String msg)
    // Purpose: To send message
    // Input:   String
    // Output:  void
    //**********************************************************/
    private void send(String msg){
        if(account.getUsername() != null){
            try {
                out.writeUTF(msg);
            }
            catch (IOException ex) {
                System.out.println("ManageSendIOException: " + ex);
            }
        }  
    }
    
    //**********************************************************/
    // Mothod:  private void manageLogin(String[] tokens)
    // Purpose: To manage login request
    // Input:   String[]
    // Output:  void
    //**********************************************************/
    private void manageLogin(String[] tokens){  
        //Check if tokens constains three tokens
        if(tokens.length == 3){  
            
            //Try to connect to the database
            try {
                dbClient = new DBClient(url, usr, pwd);
                dbClient.setDataCollector(this);
                
                //Retrieve data for account info from the database
                String queryType = "accounts";
                String sql = "SELECT username, password, user_id FROM accounts "
                           + "WHERE username = '" + tokens[1] + "' "
                           + "AND password = '" + tokens[2] + "'"; 
                
                dbClient.setQueryType(queryType);
                dbClient.query(sql);
                
                //If the account object is not equal to null
                if(account != null){
                    
                    //Retrieve data for user info from the database
                    queryType = "users";
                    sql = "SELECT role FROM users WHERE user_id = '" + account.getUserID() + "'";
                    
                    dbClient.setQueryType(queryType);
                    dbClient.query(sql);
                    
                    //Try response to the client socket
                    try {
                        String msgForLogin = "login " + role;
                        out.writeUTF(msgForLogin);
                        System.out.println(account.getUsername() + " is online!");
                        
                        //Send information about other user who is online to the client
                        List<ClientManager> managerList = server.getManagerList();
                        for(ClientManager manager : managerList){
                            if(manager.getCurrentUser()!= null){
                                if(!account.getUsername().equals(manager.getCurrentUser())){
                                    String msgOnlineOne = "online " + manager.getCurrentUser() + "\n";
                                    send(msgOnlineOne);
                                }
                            }
                        }
                        
                        //Send information about other user who is online to the client
                        String msgOnlineTwo = "online " + account.getUsername() + "\n";
                        for(ClientManager manager : managerList){
                            if(!account.getUsername().equals(manager.getCurrentUser())){
                                manager.send(msgOnlineTwo);
                            }
                        }
                    }
                    catch (IOException ex) {
                        System.out.println("ManageLoginIOException: " + ex);
                    }
                }
                //Otherwise response non-login if the account dosen't exist
                else{
                    try {
                        String msg = "non-login";
                        out.writeUTF(msg);
                        System.out.println("Connection failed");
                    }
                    catch (IOException ex) {
                        System.out.println("ManageNon-loginIOException: " + ex);
                    }
                }
            }
            catch (SQLException e) {
                System.out.println("SQLException: " + e);
            }
            catch (Exception e) {
                System.out.println("Exception: " + e);
            }
        }
        else{
            try {
                String msg = "Unknown tokens formate\n";
                out.writeUTF(msg);
            }
            catch (IOException ex) {
                System.out.println("UnknownTokensFormateIOException: " + ex);
            }
        }
    }

    //**********************************************************/
    // Mothod:  private void manageLogOut()
    // Purpose: To manage log out request
    // Input:   None
    // Output:  void
    //**********************************************************/
    private void manageLogOut(){     
        //Server remove the current manager
        server.removeManager(this);
        //Regain manager list
        List<ClientManager> managerList = server.getManagerList();
        
        //Send information about other user who is offlin to the client
        String msgForOffline = "offline " + account.getUsername() + "\n";
        for(ClientManager manager : managerList){
            if(!account.getUsername().equals(manager.getCurrentUser())){
                manager.send(msgForOffline);
            }
        }
    }

    //**********************************************************/
    // Mothod:  private void manageMessage(String[] tokens)
    // Purpose: To manage message where to send to and 
    //          it was from 
    // Input:   String[]
    // Output:  void
    //**********************************************************/
    private void manageMessage(String[] tokens){       
        
        //Assign variables
        String sendTo = tokens[1];
        String msgBody = tokens[2];
        
        //Assign boolean variable that will check if client request
        //for grouped communication 
        boolean isTopic = sendTo.charAt(0) == '#';
        
        //Get current manager list
        List<ClientManager> managerList = server.getManagerList();
         
        //Look for the end-user or room to send the message
        for(ClientManager manager : managerList){  
            //Check for grouped conversation
            if(isTopic){
                //Match the room to send message
                if(manager.isMemberOfTopic(sendTo)){
                    String outMsg = "msg " + sendTo + ":" + account.getUsername() + " " + msgBody + "\n";
                    manager.send(outMsg);
                }
            }
            //Check for person-to-person conversation
            else{
                //Match the end-user to send message
                if(sendTo.equalsIgnoreCase(manager.getCurrentUser())){
                    String outMsg = "msg " + account.getUsername() + " " + msgBody + "\n";
                    manager.send(outMsg);
                }
            }
        }
    }

    //**********************************************************/
    // Mothod:  private void manageJoin(String[] tokens)
    // Purpose: To manage join request
    // Input:   String[]
    // Output:  void
    //**********************************************************/
    private void manageJoin(String[] tokens) {
        
        //Check tokens and create a room for the grouped conversation
        if (tokens.length > 1){
            String topic = tokens[1];
            topicSet.add(topic);    
        }
    }
    
    //**********************************************************/
    // Mothod:  private void manageLeave(String[] tokens)
    // Purpose: To manage leave request
    // Input:   String[]
    // Output:  void
    //**********************************************************/
    private void manageLeave(String[] tokens) {
        
        //Check tokens and remove the room of the grouped conversation
        if (tokens.length > 1){
            String topic = tokens[1];
            topicSet.remove(topic);     
        }
    }
    
    //**********************************************************/
    // Mothod:  public boolean isMemberOfTopic(String topic)
    // Purpose: To check if current manage is part of the
    //          grouped conversation room
    // Input:   String
    // Output:  boolean
    //**********************************************************/
    public boolean isMemberOfTopic(String topic){
        return topicSet.contains(topic);
    }
}
