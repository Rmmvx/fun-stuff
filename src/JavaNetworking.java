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
    
    /**
     * readsMessages from beagleBone
     */
    private BufferedReader readMessage;

    /**
     * sends messages to beagleBone
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
            this.readMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.sendMessage = new PrintWriter(socket.getOutputStream(), true);
            
            //Sends string to beaglebone
            sendMessage.print(serverPass);
            sendMessage.flush();

            String check = null;
            check = readMessage.readLine();

            //Checks if beaglebone sends correct password
            if (check == null || !check.equals((clientPass))){
                sendMessage.close();
                readMessage.close();
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
        int output = 0;
        if (socket == null){
            return -1;
        }
        try{
            this.sendMessage.print("0");
            this.sendMessage.flush();
            //if method call on beaglebone is successful, we receive "0" as confirmation
            //Next few lines check for this message.
            String confirmed = null;
            confirmed = this.readMessage.readLine();
            if(confirmed == null || !confirmed.equals("0")){
                output = -1;
                return output;
            }
        }
        catch(IOException e){
            output = -1;
        }
        return output;
    }
    
    /** Sends comnand to beablebone to move right
     * 
     * @param socket
     * @return 0 if success
     */
    public int moveRight(Socket socket){
        int output = 0;
        if (socket == null){
            return -1;
        }
        try{
            this.sendMessage.print("1");
            this.sendMessage.flush();
            //if method call on beaglebone is successful, we receive "0" as confirmation
            //Next few lines check for this message.
            String confirmed = null;
            confirmed = this.readMessage.readLine();
            if(confirmed == null || !confirmed.equals("1")){
                output = -1;
                return output;
            }
        }
        catch(IOException e){
            output = -1;
        }
        return output;
    }

    /** Sends command to move forwards
     * @param socket
     * @return 0 if success
     */
    public int moveForward(Socket socket){
        int output = 0;
        if (socket == null){
            return -1;
        }
        try{
            this.sendMessage.print("2");
            this.sendMessage.flush();
            //if method call on beaglebone is successful, we receive "0" as confirmation
            //Next few lines check for this message.
            String confirmed = null;
            confirmed = this.readMessage.readLine();
            if(confirmed == null || !confirmed.equals("2")){
                output = -1;
                return output;
            }
        }
        catch(IOException e){
            output = -1;
        }
        return output;
    }

    /** Sends command to either back up
     * 
     * @param socket
     * @return 0 if success
     */
    public int moveInReverse(Socket socket){
        int output = 0;
        if (socket == null){
            return -1;
        }
        try{
            this.sendMessage.print("3");
            this.sendMessage.flush();
            //if method call on beaglebone is successful, we receive "0" as confirmation
            //Next few lines check for this message.
            String confirmed = null;
            confirmed = this.readMessage.readLine();
            if(confirmed == null || !confirmed.equals("3")){
                output = -1;
                return output;
            }
        }
        catch(IOException e){
            output = -1;
        }
        return output;
    }
    /** This method sends the command to start video streaming
     * 
     * @param socket
     * 
     * @return true if stream starts successfully
     */
    public boolean startStreaming(Socket socket){
        if (socket == null){
            return false;
        }
        try{
            this.sendMessage.print("4");
            this.sendMessage.flush();

            String confirmed = null;
            confirmed = this.readMessage.readLine();
            if (confirmed == null || !confirmed.equals("Streaming")){
                return false;
            }
            else{
                return true;
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
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
        else{
            try{
                this.sendMessage.print("5");
                this.sendMessage.flush();

                String confirmed = null;
                confirmed = this.readMessage.readLine();
                if (confirmed == null || !confirmed.equals("Not streaming")){
                    flag = false;
                }
                else{
                    flag = true;
                }
            }
            catch(IOException e){
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }

    /** This method closes the open socket and any other resource
     * @param socket
     * @return 0 if success, -1 if any error
     */
    public int cleanUp(Socket socket){
        if (socket != null){
            try{
                socket.close();
                readMessage.close();
                sendMessage.close();
                stopStreaming(socket);
            }
            catch(IOException e){
                e.printStackTrace();
                return -1;
            }
        }
        return 0;
    }
    /** Main method for debugging
     * 
     * @param args
     */
    public static void main(String[] args){
        JavaNetworking j = new JavaNetworking();
        Socket s = j.createConnection(12345, LOCAL_HOST, "none", "none");
        if (s.isConnected()){
            System.out.println("success connecting!");
            try{
                System.out.println(j.moveLeft(s));
                System.out.println(j.moveRight(s));
                System.out.println(j.moveForward(s));
                System.out.println(j.moveInReverse(s));
                System.out.println(j.startStreaming(s));
                System.out.println(j.stopStreaming(s));
                s.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Unsuccessful, socket is closed");
        }
    }
}
