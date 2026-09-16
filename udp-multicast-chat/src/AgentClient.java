import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class AgentClient {

    final static String INET_ADDR = "230.0.0.1";
    final static int PORT = 8547;
    final static int BUFFER_SIZE = 2048;

    public static void main(String[] args) {
        InetAddress group;
        try {
            group = InetAddress.getByName(INET_ADDR);

            // 1) Choisir une interface IPv4 qui supporte multicast
            NetworkInterface ni = pickIPv4MulticastInterface();
            System.out.println("Using interface: " + ni.getDisplayName());

            try (MulticastSocket clientSocket = new MulticastSocket(PORT)) {

                // 2) Forcer l’interface
                clientSocket.setNetworkInterface(ni);
                clientSocket.setReuseAddress(true);

                // 3) Join group (version moderne)
                clientSocket.joinGroup(new InetSocketAddress(group, PORT), ni);

                System.out.println("AgentClient started. Waiting for messages...");

                while (true) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    DatagramPacket msgPacket = new DatagramPacket(buffer, buffer.length);
                    clientSocket.receive(msgPacket);

                    String receivedMessage = new String(
                            msgPacket.getData(),
                            msgPacket.getOffset(),
                            msgPacket.getLength(),
                            StandardCharsets.UTF_8
                    );

                    System.out.println("Received: " + receivedMessage);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static NetworkInterface pickIPv4MulticastInterface() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();

            if (!ni.isUp() || ni.isLoopback() || !ni.supportsMulticast()) continue;

            // Vérifier qu’il y a au moins une adresse IPv4
            Enumeration<InetAddress> addrs = ni.getInetAddresses();
            while (addrs.hasMoreElements()) {
                InetAddress a = addrs.nextElement();
                if (a instanceof Inet4Address) {
                    return ni;
                }
            }
        }

        throw new SocketException("No IPv4 multicast-capable interface found (Wi-Fi/Ethernet).");
    }
}