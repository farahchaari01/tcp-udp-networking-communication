import java.io.*;
import java.net.*;
import java.util.*;

public class Serveur {
    private static final int PORT = 1111;

    // synchronizedMap + synchronized blocks when iterating
    private static final Map<String, PrintWriter> connectedClients =
            Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur en attente de connexions sur le port " + PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connexion : " + clientSocket.getRemoteSocketAddress());

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("Erreur du serveur : " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String clientName;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // 1) Lire le nom
                String proposedName = reader.readLine();
                if (proposedName == null || proposedName.trim().isEmpty()) {
                    writer.println("Nom invalide. Déconnexion.");
                    return;
                }
                proposedName = proposedName.trim();

                // 2) Refuser si déjà utilisé
                synchronized (connectedClients) {
                    if (connectedClients.containsKey(proposedName)) {
                        writer.println("NOM_DEJA_UTILISE");
                        return;
                    }
                    clientName = proposedName;
                    connectedClients.put(clientName, writer);
                }

                System.out.println(clientName + " a rejoint la discussion.");

                // 3) Messages système cohérents avec le client
                writer.println("BIENVENUE " + clientName);
                broadcastMessage("ARRIVEE " + clientName);

                sendConnectedMembersList(); // broadcast liste

                // 4) Instructions
                writer.println("Instructions :");
                writer.println("- Message public : tapez votre message puis Entrée");
                writer.println("- Message privé : /msg <nomDestinataire> <message>");
                writer.println("- Liste connectés : /liste");
                writer.println("- Quitter : /quit");

                // 5) Boucle lecture messages
                String message;
                while ((message = reader.readLine()) != null) {
                    message = message.trim();
                    if (message.isEmpty()) continue;

                    if (message.equals("/quit")) {
                        writer.println("Déconnexion...");
                        break;
                    } else if (message.startsWith("/msg ")) {
                        sendPrivateMessage(message);
                    } else if (message.equals("/liste")) {
                        sendConnectedMembersListToClient();
                    } else {
                        broadcastMessage(clientName + ": " + message);
                    }
                }
            } catch (IOException e) {
                System.err.println("Erreur de gestion du client : " + e.getMessage());
            } finally {
                cleanup();
            }
        }

        private void cleanup() {
            if (clientName != null) {
                synchronized (connectedClients) {
                    connectedClients.remove(clientName);
                }
                broadcastMessage("DEPART " + clientName);
                sendConnectedMembersList();
                System.out.println(clientName + " a quitté la discussion.");
            }

            try {
                clientSocket.close();
            } catch (IOException ignored) {}
        }

        private void broadcastMessage(String message) {
            synchronized (connectedClients) {
                for (PrintWriter client : connectedClients.values()) {
                    client.println(message);
                }
            }
        }

        private void sendPrivateMessage(String message) {
            // Format : /msg <dest> <contenu...>
            String[] parts = message.split(" ", 3);
            if (parts.length < 3) {
                writer.println("Usage : /msg <destinataire> <message>");
                return;
            }

            String recipient = parts[1].trim();
            String content = parts[2].trim();

            PrintWriter recipientWriter;
            synchronized (connectedClients) {
                recipientWriter = connectedClients.get(recipient);
            }

            if (recipientWriter != null) {
                recipientWriter.println("(Message privé de " + clientName + ") " + content);
                writer.println("(À " + recipient + ") " + content);
            } else {
                writer.println("Utilisateur non trouvé : " + recipient);
            }
        }

        private void sendConnectedMembersListToClient() {
            synchronized (connectedClients) {
                writer.println("Liste des personnes connectées : " + String.join(", ", connectedClients.keySet()));
            }
        }

        private void sendConnectedMembersList() {
            synchronized (connectedClients) {
                broadcastMessage("Liste des personnes connectées : " + String.join(", ", connectedClients.keySet()));
            }
        }
    }
}