package P9;

import java.util.concurrent.*;

public class usaProdConAltoNivel {
    public static void ProductorConsumidor(int tam, int con, int prod)
    {
        PCMonitorAltoNivel Mon = new PCMonitorAltoNivel(tam);
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
    PCMonitorAltoNivel m;
    public Consumidor(PCMonitorAltoNivel Mon)
    {
        m = Mon;
    }

    public void run()
    {
        for(int i = 0; i < 100; ++i)
        {
            int res = m.Take();
            System.out.println("Se ha consumido " + (i + 1) + " veces con res = " + res);
        }
        System.out.println("Consumidor fin");
    }
}

class Productor implements Runnable
{
    PCMonitorAltoNivel m;
    public Productor(PCMonitorAltoNivel Mon)
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
