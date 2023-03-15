/* This class is responsible for connecting and communicating with
 * the main python class in the beaglebone.
 * 
 */

import java.net.*;
import java.io.*;

public class clientController{
    /**
     * Local host IP address used for testing
     */
    private String localHost = "127.0.0.1";
    /**
     * Variable to keep track of messages from server
     */
    private BufferedReader receivedMessage;

    /**
     * variable that sends GUI input to server
     */
    private PrintWriter sendMessage;


    /** This method listens in a specified port
     * if a connection can be made, it returns the socket through which
     * communication can be made.
     * 
     * @param portNumber user specified port number
     * @param password message recognized by our beaglebone server program
     * 
     * @return socket through which communication is possible.
     */
    public Socket createConnection(int portNumber, String hostName, String clientPass, String serverPass){
        Socket socket = null;
        try{
            socket = new Socket(hostName, portNumber);
            receivedMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sendMessage = new PrintWriter(socket.getOutputStream(), true);
            
            //Sends string to beaglebone
            sendMessage.print(serverPass);

            //Checks if beaglebone sends correct password
            if (!receivedMessage.readLine().equals(clientPass)){
                System.out.println("Incorrect beaglebone device");
                return null;
            }
        }
        catch(IOException e){
            //if a connection cannot be made.
            e.printStackTrace();
            socket = null;
        }

        return socket;
    }
}