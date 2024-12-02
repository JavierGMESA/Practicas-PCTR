package P9;

import java.util.concurrent.Semaphore;

public class cuentaCorrienteSem 
{
    public int saldo;
    public Semaphore mutex;
    public Semaphore seco;
    
    public cuentaCorrienteSem()
    {
        saldo = 0;
        mutex = new Semaphore(1);
    }

    public void ingreso(int cuantia)
    {
        try
        {
            mutex.acquire();
        }catch(Exception e){}
        saldo += cuantia;
        mutex.release();
    }

    synchronized public void reintegro(int cuantia)
    {
        try
        {
            mutex.acquire();
        }catch(Exception e){}
        try
        {
            while(saldo < cuantia)
            {
                mutex.release();
                mutex.acquire();
            }
        }catch(Exception e){}
        saldo -= cuantia;
        mutex.release();
    }
    
    public int valor()
    {
        return saldo;
    }
}
