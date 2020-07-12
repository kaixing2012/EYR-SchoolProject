package mathquiz.networks;

//Import IO libraries
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//Import Net libraries
import java.net.Socket;
import java.net.SocketException;

//Import util libraries
import java.util.ArrayList;

//Import third party libraries
import org.apache.commons.lang3.StringUtils;

//**********************************************************/
// Filename: Client.java
// Purpose:  To create client API that works with ClientManager
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mql-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class Client {

    //Declare variable for locations
    private final String host;
    private final int port;
    
    //Declare variable for network
    private Socket socket;
    
    //Declare variable for input and output data
    private DataOutputStream serverOut;
    private DataInputStream serverIn;
    
    //Deaclare data structure for interface listeners
    private final ArrayList<IUserStatusListener> userStatusListeners = new ArrayList<>();
    private final ArrayList<IMessageListener> messageListeners = new ArrayList<>();
    
    private String role;
    
    //Construction
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getRole(){
        return this.role;
    }
    
    //**********************************************************/
    // Mothod:  public boolean connect()
    // Purpose: To connect to an address inputed
    // Input:   None
    // Output:  boolean
    //**********************************************************/
    public boolean connect(){       
        //Try to connect to the location aiming to and return true if connected
        try {
            this.socket = new Socket(host, port);
            System.out.println("Client is at the port of: " + socket.getLocalPort() + "\n");      
            this.serverOut = new DataOutputStream(socket.getOutputStream());
            this.serverIn = new DataInputStream(socket.getInputStream());
            return true;
        }
        catch(SocketException se) {
            System.out.println("ClientConnectSocketException: " + se);
            return false;
        }
        catch (IOException ex) {
            System.out.println("ClientConnectIOException: " + ex);
            return false;
        }
    }
    
    //**********************************************************/
    // Mothod:  public boolean login(String username, 
    //                               String password)
    // Purpose: To send a login request to server that connects
    //          to the server socket
    // Input:   String, String
    // Output:  boolean
    //**********************************************************/
    public boolean login(String username, String password){
        
        try {
            //Declare a command as a token
            String cmd = "login " + username + " " + password + "\n";
            //Send the command to location connected to
            serverOut.writeUTF(cmd);
            
            //Read messages from the other side in the network
            String[] received = StringUtils.split(serverIn.readUTF());
            System.out.println("Server side responses: " + received);
            
            //Check if the other side has return a permission to log
            //into the system and return true if permitted 
            if("login".equalsIgnoreCase(received[0])){
                this.role = received[1];
                System.out.println(this.role);
                this.begin();
                return true;
            }
            else{
                return false;
            }
        }
        catch (IOException ex) {
            System.out.println("ClientLoginIOException: " + ex);
            return false;
        }
    }
    
    //**********************************************************/
    // Mothod:  public boolean logout()
    // Purpose: To send a logout request to server that disconnet 
    //          the connection to the server socket
    // Input:   None
    // Output:  void
    //**********************************************************/
    public void logOut(){
        try {
            //Declare a command of logoff
            String cmd = "logoff\n";
            
            //Send a request to the other side of network
            serverOut.writeUTF(cmd);
        }
        catch (IOException ex) {
            System.out.println("ClientLogOutIOException: " + ex.toString());
        }
    }
    
    //**********************************************************/
    // Mothod:  public void join(String topic)
    // Purpose: To send a join request to server that generates 
    //          a room for clents to join and have a grouped 
    //          conversation
    // Input:   String
    // Output:  void
    //**********************************************************/
    public void join(String topic){
        try {
            //Declare a command as a request to join a group
            //# mark followed by topic name is the name of room
            String cmd = "join #" + topic + "\n";
            
            //Send the command to the other side of network
            serverOut.writeUTF(cmd);
        }
        catch (IOException ex) {
            System.out.println("ClientJoinIOException: " + ex.toString());
        } 
    }
    
    //**********************************************************/
    // Mothod:  public void leave(String topic)
    // Purpose: To send a leave request to server that leave 
    //          the grouped conversation room
    // Input:   String
    // Output:  void
    //**********************************************************/
    public void leave(String topic){
        try {
            //Declare a command as a request to leave from a group
            //# mark followed by topic name is the name of room
            String cmd = "leave  #" + topic + "\n";
            
            //Send the command to the other side of network
            serverOut.writeUTF(cmd);
        }
        catch (IOException ex) {
            System.out.println("ClientJoinIOException: " + ex.toString());
        } 
    }
    
    //**********************************************************/
    // Mothod:  public void send(String sendTo, String body)
    // Purpose: To send messages to a client from the other
    // Input:   String
    // Output:  void
    //**********************************************************/
    public void send(String sendTo, String body){   
        try {
            //Declare a command as a request to send messagies
            //to designated user in the network
            String cmd = "msg " + sendTo + " " + body + "\n";
            
            //Send the command to the designated user in network.
            serverOut.writeUTF(cmd);
        }
        catch (IOException ex) {
            System.out.println("ClientSendIOException: " + ex);
        }
    }
    
    //**********************************************************/
    // Mothod:  private void begin()
    // Purpose: To create a thread that is joinable for many 
    //          client to connect to the server socket
    // Input:   None
    // Output:  void
    //**********************************************************/
    private void begin(){   
        //Declare a new thread with override the run method
        Thread t = new Thread(){
            @Override
            public void run(){ 
                try {
                    //Keep reading data
                    dataReader();
                }
                catch (IOException ex) {
                    System.out.println("ClientBeginIOException: " + ex);
                }
            }
        };
        t.start();
    }
    
    //**********************************************************/
    // Mothod:  private void dataReader()
    // Purpose: To read response from the server
    // Input:   None
    // Output:  void
    //**********************************************************/
    private void dataReader() throws IOException{    
        try {
            String line;
            
            //Keep looping and reading command from the other side
            while((line = serverIn.readUTF()) != null){
                
                //Split string or tokens from the other side
                String[] tokens = StringUtils.split(line);
            
                //Check if the token has data returned 
                if(tokens != null && tokens.length > 0){

                    String cmd = tokens[0];
                    
                    //Check first index in the token that has return as
                    //OMLINE then insert token to the readOnline method
                    if("online".equalsIgnoreCase(cmd)){
                        readOnline(tokens); 
                    }
                    
                    //Check first index in the token that has return as
                    //OFFLINE then insert token to the readOffline method
                    else if("offline".equalsIgnoreCase(cmd)){
                        readOffline(tokens);
                    }
                    
                    //Check first index in the token that has return as
                    //MSG then insert token to the readMessage method
                    else if("msg".equalsIgnoreCase(cmd)){
                        String[] msgFrom = StringUtils.split(line, null, 3); 
                        readMessage(msgFrom);
                    }
                }
            }
        }
        catch (IOException ex) {
            System.out.println("DataReader exception: " + ex.toString());
            socket.close();
        }
    }
    
    //**********************************************************/
    // Mothod:  private void readOnline(String[] tokens)
    // Purpose: To read users who are online
    // Input:   String[]
    // Output:  void
    //**********************************************************/
    private void readOnline(String[] tokens) {
        String user = tokens[1];
        //Loop to check users online and 
        //pass online user into online method
        for (IUserStatusListener listener : userStatusListeners){
            listener.online(user);
        }
    }
    
    //**********************************************************/
    // Mothod:  private void readOffline(String[] tokens)
    // Purpose: To read users who are offline
    // Input:   String[]
    // Output:  void
    //**********************************************************/
    private void readOffline(String[] tokens) {
        String user = tokens[1];
        //Loop to check users offline and 
        //pass offline user into offline method
        for (IUserStatusListener listener : userStatusListeners){
            listener.offline(user);
        }
    }
    
    //**********************************************************/
    // Mothod:  private void readOffline(String[] tokens)
    // Purpose: To read message from the sender
    // Input:   String[]
    // Output:  void
    //**********************************************************/
    private void readMessage(String[] tokensMsg) {
        String user = tokensMsg[1];
        String msgBody = tokensMsg[2];      
        for (IMessageListener listener : messageListeners){
            listener.onMessage(user, msgBody);
        }
    }

    //**********************************************************/
    // Mothod:  public void addUserStatusListener(
    //                             IUserStatusListener listener)
    // Purpose: To add user listener into the list if 
    //          the user is online
    // Input:   IUserStatusListener
    // Output:  void
    //**********************************************************/
    public void addUserStatusListener(IUserStatusListener listener){
        userStatusListeners.add(listener);
    }
    
    //**********************************************************/
    // Mothod:  public void removeUserStatusListener(
    //                             IUserStatusListener listener)
    // Purpose: To reomve user listener from the list if the user 
    //          is offlin
    // Input:   IUserStatusListener
    // Output:  void
    //**********************************************************/
    public void removeUserStatusListener(IUserStatusListener listener){
        userStatusListeners.remove(listener);
    }
    
    //**********************************************************/
    // Mothod:  public void addMessageListener(
    //                             IMessageListener listener)
    // Purpose: To add a message listener into a list
    // Input:   IMessageListener
    // Output:  void
    //**********************************************************/
    public void addMessageListener(IMessageListener listener){
        messageListeners.add(listener);
    }
    
    //**********************************************************/
    // Mothod:  public void removeMessageListener(
    //                             IMessageListener listener)
    // Purpose: To remove a message listener from a list
    // Input:   IMessageListener
    // Output:  void
    //**********************************************************/
    public void removeMessageListener(IMessageListener listener){
        messageListeners.remove(listener);
    }
}
