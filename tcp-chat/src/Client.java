import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1111)) {
            System.out.println("Connecté au serveur de chat.");

            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            System.out.print("Entrer votre nom : ");
            String clientName = scanner.nextLine().trim();
            serverWriter.println(clientName);

            // Vérifier si le serveur refuse le nom
            socket.setSoTimeout(2000); // attendre 2s max pour le 1er message
            String firstMsg;
            try {
                firstMsg = serverReader.readLine();
            } catch (SocketTimeoutException e) {
                firstMsg = null;
            }
            socket.setSoTimeout(0); // revenir à normal

            if (firstMsg != null && firstMsg.equals("NOM_DEJA_UTILISE")) {
                System.out.println("Ce nom est déjà utilisé. Relance le client avec un autre nom.");
                return;
            }

            // Si on a reçu un premier message, l'afficher avec la logique du reader
            if (firstMsg != null) {
                System.out.println(formatServerMessage(firstMsg));
            }

            // Thread pour lire les messages du serveur
            Thread messageReaderThread = new Thread(new MessageReader(serverReader));
            messageReaderThread.start();

            // Envoyer les messages au serveur
            while (true) {
                String messageToSend = scanner.nextLine();
                serverWriter.println(messageToSend);

                if (messageToSend.trim().equals("/quit")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur du client : " + e.getMessage());
        }
    }

    private static String formatServerMessage(String msg) {
        if (msg.startsWith("BIENVENUE")) {
            return msg;
        } else if (msg.startsWith("ARRIVEE")) {
            String[] parts = msg.split(" ", 2);
            return (parts.length > 1) ? (parts[1] + " a rejoint la discussion.") : msg;
        } else if (msg.startsWith("DEPART")) {
            String[] parts = msg.split(" ", 2);
            return (parts.length > 1) ? (parts[1] + " a quitté la discussion.") : msg;
        }
        return msg;
    }

    private static class MessageReader implements Runnable {
        private final BufferedReader serverReader;

        public MessageReader(BufferedReader serverReader) {
            this.serverReader = serverReader;
        }

        public void run() {
            try {
                String receivedMessage;
                while ((receivedMessage = serverReader.readLine()) != null) {
                    System.out.println(formatServerMessage(receivedMessage));
                }
            } catch (IOException e) {
                System.err.println("Erreur de lecture des messages : " + e.getMessage());
            }
        }
    }
}