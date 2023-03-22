import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.*;

public class JavaNetworkingTest {


    @Test
    public void testFailedCreateConnection() {
        JavaNetworking networkingObject = new JavaNetworking();
        Socket testSocket = networkingObject.createConnection(12345, JavaNetworking.LOCAL_HOST, null, null);
        assertNull(testSocket);
    }

    @Test
    public void testMove() {
        Socket socket = null;
        try{
            socket = new Socket("localhost", 12345);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        assertEquals(-1, new JavaNetworking().move(socket, 'l'));
        assertEquals(-1, new JavaNetworking().move(socket, 'r'));
        assertEquals(-1, new JavaNetworking().move(socket, 'f'));
        assertEquals(-1, new JavaNetworking().move(socket, 'b'));
    }

    @Test
    public void testStartStreaming() {
        Socket socket = null;
        assertFalse(new JavaNetworking().startStreaming(socket));
    }

    @Test
    public void testStopStreaming() {
        Socket socket = null;
        assertTrue(new JavaNetworking().stopStreaming(socket));
    }    
    
    @Test
    public void testCleanUp() {
        assertEquals(0, new JavaNetworking().cleanUp(new Socket()));
    }
}
