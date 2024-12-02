package P9;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.*;

public class tiempos 
{
    public static void main(String[] args) throws Exception
    {
        ExecutorService pool = Executors.newFixedThreadPool(9);
        for(int i = 0; i < 3; ++i)
        {
            pool.submit(new UsaIncSynchronized());
        }
        for(int i = 0; i < 3; ++i)
        {
            pool.submit(new UsaIncLock());
        }
        for(int i = 0; i < 3; ++i)
        {
            pool.submit(new UsaIncAtomic());
        }
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.DAYS);
        System.out.println("El mejor tiempo del Syncrhonized es " + UsaIncSynchronized.mejor_tiempo);
        System.out.println("El mejor tiempo del Locks es " + UsaIncLock.mejor_tiempo);
        System.out.println("El mejor tiempo del Atomic es " + UsaIncAtomic.mejor_tiempo);

        //IMPORTANTE: DONDE SE OBTIENE EL MEJOR TIEMPO ES CON EL SYNCHRONIZED
    }
}

class Increment
{
    private static int n = 0, n2 = 0;
    private static Object l = new Object();
    private static ReentrantLock lo = new ReentrantLock();
    private static AtomicInteger n3 = new AtomicInteger(0);

    public static long f1(long iter)
    {
        long inicio, fin;
        inicio = System.nanoTime();
        for(long i = 0; i < iter; ++i)
        {
            synchronized(l)
            {
                ++n;
            }
        }
        fin = System.nanoTime();
        return(fin - inicio);
    }

    public static long f2(long iter)
    {
        long inicio, fin;
        inicio = System.nanoTime();
        for(long i = 0; i < iter; ++i)
        {
            lo.lock();
            try
            {
                ++n2;
            }finally
            {
                lo.unlock();
            }
        }
        fin = System.nanoTime();
        return(fin - inicio);
    }

    public static long f3(long iter)
    {
        long inicio, fin;
        inicio = System.nanoTime();
        for(long i = 0; i < iter; ++i)
        {
            n3.incrementAndGet();
        }
        fin = System.nanoTime();
        return(fin - inicio);
    }
}

class UsaIncSynchronized implements Runnable
{
    public static long mejor_tiempo = 100000000;
    private static Object l = new Object();

    public void run()
    {
        long tiempo = Increment.f1(100);
        synchronized(l)
        {
            if(tiempo < mejor_tiempo)
            {
                mejor_tiempo = tiempo;
            }
        }
    }
}

class UsaIncLock implements Runnable
{
    public static long mejor_tiempo = 100000000;
    private static Object l = new Object();

    public void run()
    {
        long tiempo = Increment.f2(100);
        synchronized(l)
        {
            if(tiempo < mejor_tiempo)
            {
                mejor_tiempo = tiempo;
            }
        }
    }
}

class UsaIncAtomic implements Runnable
{
    public static long mejor_tiempo = 100000000;
    private static Object l = new Object();

    public void run()
    {
        long tiempo = Increment.f3(100);
        synchronized(l)
        {
            if(tiempo < mejor_tiempo)
            {
                mejor_tiempo = tiempo;
            }
        }
    }
}
