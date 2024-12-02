package P9;

import java.util.concurrent.*;

import P8.FilosofosComensalesMonitor;

public class P9 
{
    public void ejercicio1a() throws Exception
    {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        cuentaCorrienteRL c = new cuentaCorrienteRL();
        for(int i = 0; i < 3; ++i)
        {
            pool.submit(new depositor1(c));
        }
        for(int i = 0; i < 3; ++i)
        {
            pool.submit(new reintegrador1(c));
        }
        pool.shutdown(); //IMPORTANTE: shutdown() TIENE QUE IR ANTES DEL awaitTermination() SIEMPRE
        pool.awaitTermination(10, TimeUnit.DAYS);
        System.out.println("El valor de la cuenta es: " + c.valor());
    }

    public void ejercicio1b() throws Exception
    {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        cuentaCorrienteSem c = new cuentaCorrienteSem();
        for(int i = 0; i < 1; ++i)
        {
            pool.submit(new reintegrador2(c));
        }
        for(int i = 0; i < 1; ++i)
        {
            pool.submit(new depositor2(c));
        }
        pool.shutdown(); //IMPORTANTE: shutdown() TIENE QUE IR ANTES DEL awaitTermination() SIEMPRE
        pool.awaitTermination(10, TimeUnit.DAYS);
        System.out.println("El valor de la cuenta es: " + c.valor());
    }

    public void ejercicio3() throws Exception
    {
        usaLecEscAltoNivel.LectorEscritor(3, 3);
    }

    public void ejercicio4()
    {
        usaProdConAltoNivel.ProductorConsumidor(10, 5, 5);
    }

    public void ejercicio5a() throws Exception
    {
        ExecutorService pool = Executors.newFixedThreadPool(30);
        monitorImpreAltoNivel m = new monitorImpreAltoNivel(10);
        for(int i = 0; i < 20; ++i)
        {
            pool.submit(new usaMonitorImpresoras(m));
        }
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.DAYS);
        System.out.println("Todo impreso");
    }

    public void ejercicio5b() throws Exception
    {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        FiloComenMonitorAltoNivel m = new FiloComenMonitorAltoNivel(5);
        for(int i = 0; i < 5; ++i)
        {
            pool.submit(new filosofo(i, m));
        }
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.DAYS);
        System.out.println("Todos han comido");
    }    
}

class depositor1 implements Runnable
{
    private cuentaCorrienteRL c;

    public depositor1(cuentaCorrienteRL cuenta)
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

class reintegrador1 implements Runnable
{
    private cuentaCorrienteRL c;

    public reintegrador1(cuentaCorrienteRL cuenta)
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

class depositor2 implements Runnable
{
    private cuentaCorrienteSem c;

    public depositor2(cuentaCorrienteSem cuenta)
    {
        c = cuenta;
    }
    
    public void run()
    {
        for(int i = 0; i < 1; ++i)
        {
            c.ingreso(200);
        }
    }
}

class reintegrador2 implements Runnable
{
    private cuentaCorrienteSem c;

    public reintegrador2(cuentaCorrienteSem cuenta)
    {
        c = cuenta;
    }

    public void run()
    {
        for(int i = 0; i < 1; ++i)
        {
            c.reintegro(200);
        }
    }
}

class usaMonitorImpresoras implements Runnable
{
    private monitorImpreAltoNivel monitor;

    public usaMonitorImpresoras(monitorImpreAltoNivel m)
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
    private FiloComenMonitorAltoNivel m;

    public filosofo(int id, FiloComenMonitorAltoNivel monitor)
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