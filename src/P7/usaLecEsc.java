package P7;

import java.util.concurrent.*;

public class usaLecEsc {
    public static void LectorEscritor(int lec, int esc)
    {
        LEMonitor mon = new LEMonitor();
        ExecutorService pool = Executors.newFixedThreadPool((lec + esc) * 2);
        int[] elemento = new int[1];
        elemento[0] = -1;
        for(int i = 0; i < esc; ++i)
        {
            pool.execute(new Escritor(mon, elemento));
        }
        for(int i = 0; i < lec; ++i)
        {
            pool.execute(new Lector(mon, elemento));
        }
        pool.shutdown();
    }
}

class Lector implements Runnable
{
    LEMonitor m;
    int[] elemento;
    public Lector(LEMonitor mon, int[] element)
    {
        m = mon;
        elemento = element;
    }
    
    public void run()
    {
        for(int i = 0; i < 1; ++i)
        {
            m.abrirLectura();
            System.out.println("El valor del elemento es " + elemento[0]);
            m.cerrarLectura();
        }
        System.out.println("Fin lector");
    }
}

class Escritor implements Runnable
{
    LEMonitor m;
    int[] elemento;
    static int num = 0;
    public Escritor(LEMonitor mon, int[] element)
    {
        m = mon;
        elemento = element;
    }
    
    public void run()
    {
        for(int i = 0; i < 1; ++i)
        {
            m.abrirEscritura();
            elemento[0] = num;
            System.out.println("Escritura del elemento " + num);
            num += 1;
            m.cerrarEscritura();
        }
        System.out.println("Fin escritor");
    }
}
