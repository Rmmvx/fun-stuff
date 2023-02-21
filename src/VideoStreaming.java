import java.net.*;
import java.io.*;
import java.awt.image.*;
import java.awt.FlowLayout;
import javax.imageio.ImageIO;
import javax.swing.*;

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
                
                ImageIcon icon = new ImageIcon(newImage);
                JFrame nFrame = new JFrame();
                nFrame.setLayout(new FlowLayout());
                nFrame.setSize(1280,720);
                JLabel lbl = new JLabel();
                lbl.setIcon(icon);
                nFrame.add(lbl);
                nFrame.setVisible(true);
                nFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
