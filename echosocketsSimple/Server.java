package echosocketsSimple;
import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

import static echosocketsSimple.Server.clientes;

public class Server {
    public static Set<PrintWriter> clientes = new HashSet<>();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Servidor iniciado en el puerto 5000...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado: "
                    + clientSocket.getInetAddress()
                    + " Puerto:" + clientSocket.getPort());
            // Cada cliente se maneja en un hilo separado
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            synchronized (clientes){
                clientes.add(out);
            }

            out.println("¡Bienvenido al servidor!");

            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                System.out.println("Cliente dice: " + mensaje);
                synchronized (clientes){
                    for (PrintWriter writer : clientes){
                        writer.println(mensaje);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Cliente desconectado");
        }
    }
}
