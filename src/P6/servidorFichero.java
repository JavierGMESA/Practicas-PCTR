package P6;

import java.io.*;
import java.net.*;

public class servidorFichero {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Servidor esperando conexiones en el puerto 8080...");

            // Acepta una conexión de cliente
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado");

            // Recibe el archivo desde el cliente
            InputStream input = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // Crea un archivo para guardar el contenido recibido
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter("archivo_recibido.txt"));

            String line;
            while ((line = reader.readLine()) != null) {
                fileWriter.write(line);
                fileWriter.newLine(); //IMPORTANTE: CREAR UNA NUEVA LÍNEA DEL FICHERO
            }

            // Cierra los streams y el socket
            fileWriter.close();
            clientSocket.close();
            System.out.println("Archivo recibido y guardado como 'archivo_recibido.txt'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
