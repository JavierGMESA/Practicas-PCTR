package P9;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class cuentaCorrienteRL 
{
    public int saldo;
    public ReentrantLock l;
    public Condition seco;
    
    public cuentaCorrienteRL()
    {
        saldo = 0;
        l = new ReentrantLock();
        seco = l.newCondition();
    }

    public void ingreso(int cuantia)
    {
        l.lock();
        saldo += cuantia;
        seco.signal();
        l.unlock();
    }

    public void reintegro(int cuantia)
    {
        l.lock();
        try
        {
            while(saldo < cuantia)
            {
                seco.await();
            }
        }catch(Exception e){}
        saldo -= cuantia;
        l.unlock();
    }
    
    public int valor()
    {
        return saldo;
    }
}
