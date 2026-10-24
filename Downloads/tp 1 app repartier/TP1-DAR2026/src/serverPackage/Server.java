package serverPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 5000;
    private static final int OPERANDE = 5;

    private static InetAddress adresseLocale() throws IOException {
        try (DatagramSocket ds = new DatagramSocket()) {
            ds.connect(InetAddress.getByName("8.8.8.8"), 10002);
            InetAddress a = ds.getLocalAddress();
            if (!a.isAnyLocalAddress()) {
                return a;
            }
        } catch (IOException ignored) {
        }
        return InetAddress.getLocalHost();
    }

    private static double calculer(int choix, int x) {
        switch (choix) {
            case 1:
                return x + OPERANDE;
            case 2:
                return x - OPERANDE;
            case 3:
                return x * OPERANDE;
            case 4:
                return (double) x / OPERANDE;
            default:
                return Double.NaN;
        }
    }

    public static void main(String[] args) {
        try {
            InetAddress adresse = args.length > 0 ? InetAddress.getByName(args[0]) : adresseLocale();
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(adresse, PORT));
            System.out.println("Je suis un serveur en attente la connexion d'un client ");
            System.out.println("Adresse : " + adresse.getHostAddress() + " | Port : " + PORT);

            Socket socket = serverSocket.accept();
            System.out.println("un client est connecté : " + socket.getInetAddress().getHostAddress());

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            while (true) {
                int choix = in.readInt();
                if (choix == 0) {
                    System.out.println("Le client a demandé la fin de la communication");
                    break;
                }
                int x = in.readInt();
                double resultat = calculer(choix, x);
                System.out.println("Reçu : choix = " + choix + ", x = " + x + " -> résultat = " + resultat);
                out.writeDouble(resultat);
                out.flush();
            }

            in.close();
            out.close();
            socket.close();
            serverSocket.close();
            System.out.println("Sockets fermés côté serveur");
        } catch (IOException e) {
            System.out.println("Erreur serveur : " + e.getMessage());
        }
    }
}
