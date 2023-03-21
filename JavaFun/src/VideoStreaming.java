import java.net.*;
import java.io.*;
import java.awt.image.*;
import java.awt.FlowLayout;
import javax.imageio.ImageIO;
import javax.swing.*;

public class VideoStreaming{
    public static void main(String[] args){
        //Sets the frame to display video
        JFrame nFrame = new JFrame();
        nFrame.setLayout(new FlowLayout());
        nFrame.setSize(1280,720);
        JLabel lbl = new JLabel();
        
        int timer = 10000;
        try(DatagramSocket socket = new DatagramSocket(12345)){
            System.out.println("Starting");
            //Currently, endless loop
            while(timer != 0){
                byte[] buffer = new byte[65536];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                ByteArrayInputStream inStream = new ByteArrayInputStream(buffer);
                BufferedImage newImage = ImageIO.read(inStream);
                
                ImageIcon icon = new ImageIcon(newImage);

                lbl.setIcon(icon);
                nFrame.add(lbl);

                nFrame.setVisible(true);
                nFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                timer--;
                System.out.println(timer);
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
