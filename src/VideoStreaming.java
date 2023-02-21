import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class VideoStreaming{
    public static void main(String[] args){
        
        try{
            System.out.println("Starting");
            DatagramSocket socket = new DatagramSocket(12345);

            while(true){
                byte[] buffer = new byte[65536];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                System.out.println("waiting for connection");
                socket.receive(packet);
                ByteArrayInputStream inStream = new ByteArrayInputStream(buffer);
                BufferedImage newImage = ImageIO.read(inStream);
                ImageIO.write(newImage, "jpg" , new File("output.jpg"));
                System.out.println("image generated");
            }
        }
        catch(SocketException e){
            System.out.println("Socked timed out");
        }
        catch(IOException e){
            System.out.print("IOException");
        }

    }

}
