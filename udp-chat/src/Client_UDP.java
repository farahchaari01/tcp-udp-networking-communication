import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client_UDP {
    private final DatagramSocket socket;
    private final InetAddress serverAddress;
    private final int serverPort;
    private String clientName;

    public Client_UDP(String serverHost, int serverPort) throws Exception {
        this.socket = new DatagramSocket();          // port local aléatoire
        this.serverAddress = InetAddress.getByName(serverHost);
        this.serverPort = serverPort;
    }

    public void startClient() {
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter your name: ");
            clientName = scanner.nextLine().trim();

            // HELLO
            send("HELLO " + clientName);

            // Thread réception
            Thread receiver = new Thread(() -> {
                try {
                    byte[] buffer = new byte[2048];
                    while (true) {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);

                        String msg = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
                        System.out.println(msg);
                    }
                } catch (Exception e) {
                    // socket closed -> stop
                }
            });
            receiver.setDaemon(true);
            receiver.start();

            // Envoi messages
            while (true) {
                System.out.print("> ");
                String text = scanner.nextLine();

                if ("exit".equalsIgnoreCase(text) || "/quit".equalsIgnoreCase(text)) {
                    send("BYE " + clientName);
                    break;
                }

                send("MSG " + clientName + ": " + text);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private void send(String msg) throws Exception {
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
        socket.send(packet);
    }

    public static void main(String[] args) throws Exception {
        new Client_UDP("localhost", 9876).startClient();
    }
}