import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Serveur_UDP {
    private final DatagramSocket socket;

    // clé = "ip:port" ; valeur = SocketAddress du client
    private final Map<String, SocketAddress> clients = new ConcurrentHashMap<>();
    // optionnel : associer ip:port -> nom
    private final Map<String, String> names = new ConcurrentHashMap<>();

    public Serveur_UDP(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void startServer() {
        System.out.println("UDP Server running on port " + socket.getLocalPort() + "...");

        byte[] buffer = new byte[2048];

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
                SocketAddress senderAddr = packet.getSocketAddress();
                String key = toKey(senderAddr);

                // Enregistrer le client si nouveau
                clients.putIfAbsent(key, senderAddr);

                if (msg.startsWith("HELLO ")) {
                    String name = msg.substring("HELLO ".length()).trim();
                    names.put(key, name);
                    System.out.println("Client joined: " + name + " (" + key + ")");
                    broadcast("ARRIVEE " + name, senderAddr); // à tout le monde sauf sender
                    continue;
                }

                if (msg.startsWith("BYE ")) {
                    String name = names.getOrDefault(key, key);
                    clients.remove(key);
                    names.remove(key);
                    System.out.println("Client left: " + name + " (" + key + ")");
                    broadcast("DEPART " + name, senderAddr);
                    continue;
                }

                // Messages normaux
                // Ici on rebroadcast à tous sauf expéditeur
                broadcast(msg, senderAddr);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcast(String msg, SocketAddress except) throws Exception {
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);

        for (SocketAddress addr : clients.values()) {
            if (addr.equals(except)) continue;

            DatagramPacket out = new DatagramPacket(data, data.length);
            out.setSocketAddress(addr);
            socket.send(out);
        }
    }

    private String toKey(SocketAddress addr) {
        // addr type: /127.0.0.1:54321
        return addr.toString();
    }

    public static void main(String[] args) throws Exception {
        new Serveur_UDP(9876).startServer();
    }
}