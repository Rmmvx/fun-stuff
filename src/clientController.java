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
    public static String localHost = "127.0.0.1";
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
            sendMessage.flush();

            String check = null;
            try{
                check = receivedMessage.readLine().toString();
            }
            catch(Exception e){
            }
            //Checks if beaglebone sends correct password
            if (check == null || !check.equals((clientPass))){
                System.out.println("Connection terminated");
                socket.close();
                return null;
            }
            else{
                return socket;
            }
        }
        catch(IOException e){
            //if a connection cannot be made.
            e.printStackTrace();
            socket = null;
        }

        return socket;
    }

    /** Main method for debugging
     * 
     * @param args
     */
    public static void main(String[] args){
        Socket s = new clientController().createConnection(12345, localHost, "none", "none");
        if (s != null){
            System.out.println("success connecting!");
            try{
                s.close();
            }
            catch(IOException e){

            }
        }
        else{
            System.out.println("Unsuccessful");
        }
    }
}