package mathquiz.networks;

//Import IO package
import java.io.IOException;

//Import Net package
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

//Import Util package
import java.util.List;
import java.util.ArrayList;

//**********************************************************/
// Filename: Server.java
// Purpose:  To create a socket for the server side
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqser-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class Server extends Thread{

    //Declare variable for port
    private final int port;
    
    //Declare variable for server socket
    private ServerSocket serverSocket;
    
    //Declare variable for manager that manage multiple clients
    private ClientManager manager;
    
    //Declare list store managers that provide service to clients
    private final ArrayList<ClientManager> managersList = new ArrayList<>();
 
    //Construction
    public Server(int serverPort) {
       this.port = serverPort;
    }
    
    //**********************************************************/
    // Mothod:  List<ClientManager> getManagerList()
    // Purpose: To get manager list
    // Input:   None
    // Output:  List<ClientManager>
    //**********************************************************/
    public List<ClientManager> getManagerList(){
        return managersList;
    }
    
    //**********************************************************/
    // Mothod:  public void removeManager(ClientManager manager)
    // Purpose: To remove manager from the list
    // Input:   ClientManager
    // Output:  void
    //**********************************************************/
    public void removeManager(ClientManager manager) {
        managersList.remove(manager);
    }
    
    //**********************************************************/
    // Mothod:  public void run(){
    // Purpose: To override run method in the Thread class which
    //          if to run server socket
    // Input:   None
    // Output:  void
    //**********************************************************/
    @Override
    public void run(){
       
        //Try to open the server socket
        try {
            serverSocket = new ServerSocket(port);

            //Keep listening connection form client side
            while(true){
                //Accept the client's connection
                Socket clientSocket = serverSocket.accept();                
                System.out.println("Server accepted connection from " + clientSocket + "\n");
                
                //Bring server and client socket to manager for arrangement 
                manager = new ClientManager(this, clientSocket);
                //Add one manager to the manager list 
                managersList.add(manager);
                //Manager start to work
                manager.start();
            }
        }
        catch(SocketException se) {
            System.out.println("ManagerSocketException: " + se);
        }
        catch (IOException ex) {
            System.out.println("ServerRunIOException: " + ex);
        }
    }
}
