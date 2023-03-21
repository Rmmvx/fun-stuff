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
            socket = null;
        }

        return socket;
    }

    /** This method sends commands to the beaglebone
     * 
     * @param socket
     * @param command either 'l', 'r', 'f', 'b', '0' , '1'
     * 
     * @return 0 if success
     */
    public int move(Socket socket, char command){
        int output = 0;
        if (socket == null){
            return -1;
        }
        try{
            String confirmed = null;
            
            switch (command) {
                case 'l':
                    System.out.println("Moving left");
                    sendMessage.print("0");
                    sendMessage.flush();
                    break;
                case 'r':
                    System.out.println("Moving right");
                    sendMessage.print("1");
                    sendMessage.flush();
                    break;
                case 'f':
                    System.out.println("Moving forward");
                    sendMessage.print("2");
                    sendMessage.flush();
                    break;
                case 'b':
                    System.out.println("Moving in reverse");
                    sendMessage.print("3");
                    sendMessage.flush();
                    break;
                case 'q':
                    System.out.println("Exiting all processes");
                    cleanUp(socket);
                    break;
            }

            confirmed = readMessage.readLine();
            if (confirmed == null){
                output = -1;
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
                stopStreaming(socket);
                this.sendMessage.close();
                this.readMessage.close();
                socket.close();
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
        if (s != null && s.isConnected()){
            System.out.println("success connecting!");
            System.out.println(j.move(s, 'l'));               
            System.out.println(j.move(s, 'r'));
            System.out.println(j.move(s, 'f'));
            System.out.println(j.move(s, 'b'));
            System.out.println(j.startStreaming(s));
            System.out.println(j.stopStreaming(s));
            System.out.println(j.move(s, 'q'));
            System.out.println(s.isClosed());
        }
        else{
            System.out.println("Unsuccessful, socket is closed");
        }
    }
}
