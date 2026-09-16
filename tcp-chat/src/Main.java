import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        System.out.println("1 = Serveur, 2 = Client");
        Scanner sc = new Scanner(System.in);
        String choix = sc.nextLine();

        if (choix.equals("1")) {
            Serveur.main(new String[]{});
        } else {
            Client.main(new String[]{});
        }
    }
}