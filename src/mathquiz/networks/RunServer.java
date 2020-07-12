package mathquiz.networks;

//**********************************************************/
// Filename: RunServer.java
// Purpose:  To open the socket of server and run the server
// Author:   Edward Y. Rogers(Hao-Che Yin)
// Version:  mqser-v1.0
// Date:     09-Sep-2019
// Tests:    UnitTest
//**********************************************************/
public class RunServer {
             
    public static void main(String[] args) {
        int port = 1201;
        Server server = new Server(port);
        server.start();
    }
}
