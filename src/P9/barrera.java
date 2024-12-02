package P9;

import java.util.concurrent.*;

public class barrera implements Runnable
{
    private CyclicBarrier bar;

    public barrera(CyclicBarrier b)
    {
        bar = b;
    }

    public void run()
    {
        System.out.println("El hilo " + Thread.currentThread().getName() + " llega a la barrera");
        try
        {
            bar.await();
        }catch(Exception e){}
        System.out.println("El hilo " + Thread.currentThread().getName() + " sale de la barrera");
    }

    public static void main(String[] args) throws Exception
    {
        barrera b = new barrera(new CyclicBarrier(3));
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for(int i = 0; i < 3; ++i)
        {
            pool.submit(b);
        }
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.DAYS);
    }
}
