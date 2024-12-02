package P9;

import java.util.concurrent.*;

public class usaLecEscAltoNivel {
    public static void LectorEscritor(int lec, int esc)
    {
        LEMonitorAltoNivel mon = new LEMonitorAltoNivel();
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
    LEMonitorAltoNivel m;
    int[] elemento;
    public Lector(LEMonitorAltoNivel mon, int[] element)
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
    LEMonitorAltoNivel m;
    int[] elemento;
    static int num = 0;
    public Escritor(LEMonitorAltoNivel mon, int[] element)
    {
        m = mon;
        elemento = element;
    }
    
    public void run()
    {
        for(int i = 0; i < 1; ++i)
        {
            m.abrirEscritura();
            System.out.println("Abre Escritura");
            elemento[0] = num;
            System.out.println("Escritura del elemento " + num);
            num += 1;
            System.out.println("Cierra Escritura");
            m.cerrarEscritura();
        }
        System.out.println("Fin escritor");
    }
}
