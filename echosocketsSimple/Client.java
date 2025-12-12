package echosocketsSimple;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        // Hilo para recibir mensajes del servidor
        new Thread(() -> {
            try {
                String mensaje;
                while ((mensaje = in.readLine()) != null) {
                    System.out.println(mensaje);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Enviar mensajes al servidor
        String texto;
        while ((texto = teclado.readLine()) != null) {
            out.println(texto);
        }
    }
}
