package P6;

import java.io.*;
import java.net.*;

public class clienteMultiple {
    public static void main (String[] args)
    {
        int i, num_tareas = 1000;
        int puerto = 2001;
        for(int j = 0; j < num_tareas; ++j)
        {
            i = (int)(Math.random()*10);
            try{
                System.out.println("Realizando conexion...");
                Socket cable = new Socket("localhost", puerto);
                System.out.println("Realizada conexion a "+cable);
                PrintWriter salida= new PrintWriter(new BufferedWriter(new OutputStreamWriter(cable.getOutputStream())));
                salida.println(i);
                salida.flush();
                System.out.println("Cerrando conexion...");
                cable.close();

            } catch (Exception e){
                    System.out.println("Error en sockets...");
            }
        }
    }//main
}
