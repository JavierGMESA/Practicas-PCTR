package P8;

import java.util.concurrent.*;

public class P8 
{
    public void ejercicio1() throws Exception
    {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        cuentaCompartida c = new cuentaCompartida();
        for(int i = 0; i < 3; ++i)
        {
            pool.submit(new depositor(c));
        }
        for(int i = 0; i < 3; ++i)
        {
            pool.submit(new reintegrador(c));
        }
        pool.shutdown(); //IMPORTANTE: shutdown() TIENE QUE IR ANTES DEL awaitTermination() SIEMPRE
        pool.awaitTermination(10, TimeUnit.DAYS);
        System.out.println("El valor de la cuenta es: " + c.valor());
    }

    public void ejercicio2() throws Exception
    {
        ExecutorService pool = Executors.newFixedThreadPool(30);
        monitorImpresoras m = new monitorImpresoras(10);
        for(int i = 0; i < 20; ++i)
        {
            pool.submit(new usaMonitorImpresoras(m));
        }
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.DAYS);
        System.out.println("Todo impreso");
    }

    public void ejercicio3() throws Exception
    {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        FilosofosComensalesMonitor m = new FilosofosComensalesMonitor(5);
        for(int i = 0; i < 5; ++i)
        {
            pool.submit(new filosofo(i, m));
        }
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.DAYS);
        System.out.println("Todos han comido");
    }
}

class depositor implements Runnable
{
    private cuentaCompartida c;

    public depositor(cuentaCompartida cuenta)
    {
        c = cuenta;
    }
    
    public void run()
    {
        for(int i = 0; i < 200; ++i)
        {
            c.ingreso(ThreadLocalRandom.current().nextInt(200));
        }
    }
}

class reintegrador implements Runnable
{
    private cuentaCompartida c;

    public reintegrador(cuentaCompartida cuenta)
    {
        c = cuenta;
    }

    public void run()
    {
        for(int i = 0; i < 100; ++i)
        {
            c.reintegro(ThreadLocalRandom.current().nextInt(200));
        }
    }
}

class usaMonitorImpresoras implements Runnable
{
    private monitorImpresoras monitor;

    public usaMonitorImpresoras(monitorImpresoras m)
    {
        monitor = m;
    }

    public void run()
    {
        int impresora;
        for(int i = 0; i < 3; ++i)
        {
            System.out.println("El hilo " + Thread.currentThread().getName() + " va a coger una impresora");
            impresora = monitor.pedirImpresora();
            System.out.println("El hilo " + Thread.currentThread().getName() + " ha cogido la impresora " + impresora);

            Thread.yield(); //Imprime

            monitor.liberarImpresora(impresora);
            System.out.println("El hilo " + Thread.currentThread().getName() + " ha liberado la impresora " + impresora);
        }
    }
}

class filosofo implements Runnable
{
    private int id;
    private FilosofosComensalesMonitor m;

    public filosofo(int id, FilosofosComensalesMonitor monitor)
    {
        this.id = id;
        this.m = monitor;
    }

    public void run()
    {
        try
        {
            for(int i = 0; i < 10; ++i)
            {
                System.out.println("El filosofo " + id + " va a coger sus palillos");
                m.cogerPalillos(id);
                System.out.println("El filosofo " + id + " coge sus palillos");

                Thread.yield(); //Come

                m.soltarPalillos(id);
                System.out.println("El filosofo " + id + " suelta sus palillos");
            }
        }catch(Exception e){System.out.println("Ha habido una excepcion: " + e);}
    }
}
