package P6;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class ServidorHiloconPool implements Runnable
{
    Socket enchufe;
    int num_tarea;
    public ServidorHiloconPool(Socket s, int num)
    {enchufe = s; num_tarea = num;}

    public void run()
    {
        try{
            BufferedReader entrada = new BufferedReader(new InputStreamReader(enchufe.getInputStream()));
            String datos = entrada.readLine();
            int j;
            int i = Integer.valueOf(datos).intValue();
            for(j=1; j<=20; j++)
            {
                System.out.println("El hilo "+num_tarea+" escribiendo el dato "+i);
            }
            enchufe.close();
            System.out.println("El hilo "+num_tarea+"cierra su conexion...");
        } catch(Exception e) {System.out.println("Error...");}
    }//run

    public static void main (String[] args)
    {
        int i = 1;
        int puerto = 2001;
            try{
                ServerSocket chuff = new ServerSocket (puerto, 3000);
                ThreadPoolExecutor tp = new ThreadPoolExecutor(1000, 5000, 90000000, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
                //IMPORTANTE: USO DE ThreadPoolExecutor

                while (true){
                    System.out.println("Esperando solicitud de conexion...");
                    Socket cable = chuff.accept();
                    System.out.println("Recibida solicitud de conexion...");
                    
                    tp.execute(new ServidorHiloconPool(cable, i));
                    ++i;
                }
          } catch (Exception e)
            {System.out.println("Error en sockets...");}
    }//main

}//ServidorHiloconPool