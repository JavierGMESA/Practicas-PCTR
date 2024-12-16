package P11;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class cPiMonteCarlo 
{
    public static void main(String[] args)
    {
        String URLRegistro = "rmi://localhost:8080/bonoloto";
        try
        {
            iPiMonteCarlo objeto = (iPiMonteCarlo) Naming.lookup(URLRegistro);
            objeto.reset();
            ExecutorService pool = Executors.newFixedThreadPool(10);
            for(int i = 0; i < 9; ++i) pool.submit(new MonteCarloHebra(objeto));
            pool.shutdown();
            pool.awaitTermination(10, TimeUnit.DAYS);
            

            //objeto.masPuntos(10000);
            System.out.println("La aproximación da " + objeto.aproxActual());
        }catch(Exception e)
        {
            System.out.println("Se ha producido una excepción a la hora de buscar el servidor");
            e.getMessage();
        }
    }
}

class MonteCarloHebra implements Runnable
{
    iPiMonteCarlo objeto;
    public MonteCarloHebra(iPiMonteCarlo objeto)
    {
        this.objeto = objeto;
    }

    public void run()
    {
        try
        {
            for(int i = 0; i < 10; ++i) objeto.masPuntos(100);
            System.out.println("Hilo terminado");
        }catch(Exception e)
        {
            e.getMessage();
        }
    }
}
