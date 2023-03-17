/** This class is responsible for connecting and communicating with
 * the main python class in the beaglebone.
 * @author Romel Munoz Valencia
 */
import java.net.*;
import java.io.*;

public class JavaNetworking {
    /**
     * Local host IP address used for testing
     */
    public static final String LOCAL_HOST = "127.0.0.1";

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
        BufferedReader receivedMessage;
        PrintWriter sendMessage;
        try{
            socket = new Socket(hostName, portNumber);
            receivedMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sendMessage = new PrintWriter(socket.getOutputStream(), true);
            
            //Sends string to beaglebone
            sendMessage.print(serverPass);
            sendMessage.flush();

            String check = null;
            check = receivedMessage.readLine();
            
            //Checks if beaglebone sends correct password
            if (check == null || !check.equals((clientPass))){
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

    /** This method sends commands to the beaglebone
     * to turn left
     * 
     * @param socket
     * @param command
     * 
     * @return 0 if success
     */
    public int moveLeft(Socket socket){
        return 0;
    }
    
    /** Sends comnand to beablebone to move right
     * 
     * @param socket
     * @return 0 if success
     */
    public int moveRight(Socket socket){
        return 0;
    }

    /** Sends command to move forwards
     * @param socket
     * @return 0 if success
     */
    public int moveForward(Socket socket){
        return 0;
    }

    /** Sends command to either back up
     * 
     * @param socket
     * @return 0 if success
     */
    public int moveInReverse(Socket socket){
        return 0;
    }
    /** This method sends the command to start video streaming
     * 
     * @param socket
     * 
     * @return true if stream starts successfully
     */
    public boolean startStreaming(Socket socket){
        return false;
    }

    /** Stops video streaming
     *  
     * @param socket
     * @return true if successful
     */
    public boolean stopStreaming(Socket socket){
        //If it's not streaming, simply return true
        boolean flag = false;
        if (!startStreaming(socket)){
            return true;
        }
        return flag;
    }
    /** Main method for debugging
     * 
     * @param args
     */
    public static void main(String[] args){
        Socket s = new JavaNetworking().createConnection(12345, LOCAL_HOST, "none", "none");
        if (s != null){
            System.out.println("success connecting!");
            try{
                s.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Unsuccessful");
        }
    }
}
