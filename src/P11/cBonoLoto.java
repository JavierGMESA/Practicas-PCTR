package P11;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.concurrent.ThreadLocalRandom;

public class cBonoLoto 
{
    public static void main(String[] args)
    {
        String URLRegistro = "rmi://localhost:8080/bonoloto";
        try
        {
            iBonoLoto objeto = (iBonoLoto) Naming.lookup(URLRegistro);
            objeto.resetServidor();
            int[] apuesta = new int[6];
            for(int i = 0; i < 6; ++i)
            {
                apuesta[i] = ThreadLocalRandom.current().nextInt() % 49 + 1;
            }
            boolean acertado = objeto.compApuesta(apuesta);
            if(acertado)
            {
                System.out.println("SE HA GANADO LA BONOLOTO");
            }
            else
            {
                System.out.println("Se ha perdido la bonoloto");
            }
        }catch(Exception e)
        {
            System.out.println("Se ha producido una excepción a la hora de buscar el servidor");
            e.getMessage();
        }
    }
}
