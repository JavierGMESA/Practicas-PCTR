package P6;

import java.io.*;
import java.net.*;

public class clienteFichero {
    public static void main(String[] args) {
        String filePath = "/home/javier/Documentos/Proyectos VSC/Prácticas-PCTR/Prácticas/archivo_a_enviar.txt"; // Ruta del archivo que se desea enviar

        try (Socket socket = new Socket("localhost", 8080)) {
            System.out.println("Conectado al servidor");

            // Abre el archivo y envía su contenido al servidor
            OutputStream output = socket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
            BufferedReader fileReader = new BufferedReader(new FileReader(filePath)); //IMPORTANTE: LEER DE UN FICHERO

            String line;
            while ((line = fileReader.readLine()) != null) {
                //System.out.println(line);
                writer.write(line);
                writer.newLine(); //IMPORTANTE: PASAR A LA SIGUIENTE LÍNEA DEL FICHERO
            }

            // Cierra los streams
            writer.flush();
            fileReader.close();
            System.out.println("Archivo enviado al servidor");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
