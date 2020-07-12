import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mathquiz.networks.Client;
import mathquiz.networks.Server;

import static org.junit.Assert.*;

public class JUTestClientSideConnectivity {

    private Client client;
    
    public JUTestClientSideConnectivity() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {

        int port = 1201;
        Server server = new Server(port);
        server.start();
        System.out.println("Set up and run server on port 1201....\n");
        
        client = new Client("localhost", port);
        System.out.println("Client side start connecting to the server side using:\n");
        System.out.println("    Host: loclehost\n" +
                           "    Port: " + port + "\n");
    }
    
    @After
    public void tearDown() {
        System.out.println("Unit test is finished!");
    }

    @Test
     public void serverConnectionTest() {          
         assertEquals(true, client.connect());
     }
}
