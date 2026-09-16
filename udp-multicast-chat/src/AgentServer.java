import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AgentServer {
    final static String GROUP_BROADCAST_ADDRESS = "230.0.0.1";
    final static int PORT = 8547;

    public static void main(String[] args) throws Exception {
        InetAddress group = InetAddress.getByName(GROUP_BROADCAST_ADDRESS);

        try (DatagramSocket serverSocket = new DatagramSocket();
             Scanner sc = new Scanner(System.in)) {

            System.out.println("AgentServer started. Type messages ('exit' to quit).");

            while (true) {
                System.out.print("> ");
                String msg = sc.nextLine();

                if ("exit".equalsIgnoreCase(msg)) break;

                byte[] data = msg.getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(data, data.length, group, PORT);
                serverSocket.send(packet);

                System.out.println("Sent: " + msg);
            }

            System.out.println("END DIFFUSION !!!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}