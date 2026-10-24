package clientPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {

    private static final int PORT = 5000;

    private static int lireEntier(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Entrée invalide, veuillez saisir un entier.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            String ip = args.length > 0 ? args[0] : null;
            if (ip == null) {
                System.out.print("Adresse IP du serveur : ");
                ip = sc.nextLine().trim();
            }

            System.out.println("Je suis un client pas encore connecté…");
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(InetAddress.getByName(ip), PORT), 5000);
            System.out.println("je suis un client connecté");

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            int choix;
            do {
                System.out.println();
                System.out.println("===== Calculatrice =====");
                System.out.println("1 - Addition");
                System.out.println("2 - Soustraction");
                System.out.println("3 - Multiplication");
                System.out.println("4 - Division");
                System.out.println("0 - Quitter");
                choix = lireEntier(sc, "Votre choix : ");

                if (choix == 0) {
                    out.writeInt(0);
                    out.flush();
                } else if (choix >= 1 && choix <= 4) {
                    int x = lireEntier(sc, "Entrez un entier x : ");
                    out.writeInt(choix);
                    out.writeInt(x);
                    out.flush();
                    double resultat = in.readDouble();
                    System.out.println("Résultat reçu du serveur : " + resultat);
                } else {
                    System.out.println("Choix invalide.");
                }
            } while (choix != 0);

            in.close();
            out.close();
            socket.close();
            System.out.println("Sockets fermés côté client");
        } catch (IOException e) {
            System.out.println("Erreur client : " + e.getMessage());
        }
    }
}
