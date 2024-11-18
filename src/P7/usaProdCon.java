package P7;

import java.util.concurrent.*;

public class usaProdCon {
    public static void ProductorConsumidor(int tam, int con, int prod)
    {
        PCMonitor Mon = new PCMonitor(tam);
        ExecutorService pool = Executors.newFixedThreadPool((con + prod) * 2);
        for(int i = 0; i < prod; ++i)
        {
            pool.execute(new Productor(Mon));
        }
        for(int i = 0; i < con; ++i)
        {
            pool.execute(new Consumidor(Mon));
        }
        pool.shutdown();
    }
}

class Consumidor implements Runnable
{
    PCMonitor m;
    public Consumidor(PCMonitor Mon)
    {
        m = Mon;
    }

    public void run()
    {
        for(int i = 0; i < 100; ++i)
        {
            int res = m.Take();
            System.out.println("Se ha consumido " + (i + 1) + " veces");
        }
        System.out.println("Consumidor fin");
    }
}

class Productor implements Runnable
{
    PCMonitor m;
    public Productor(PCMonitor Mon)
    {
        m = Mon;
    }

    public void run()
    {
        for(int i = 0; i < 100; ++i)
        {
            m.Append(i);
            System.out.println("Se ha producido " + (i + 1) + " veces");
        }
        System.out.println("Productor fin");
    }
}
