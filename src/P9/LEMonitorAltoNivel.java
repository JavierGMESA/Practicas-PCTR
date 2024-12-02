package P9;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LEMonitorAltoNivel 
{
    boolean escribiendo;
    int nl, nle;
    ReentrantLock l;
    Condition lector, escritor;

    public LEMonitorAltoNivel()
    {
        escribiendo = false;
        nl = nle = 0;
        l = new ReentrantLock();
        lector = l.newCondition();
        escritor = l.newCondition();
    }

    public void abrirLectura()
    {
        l.lock();
        try
        {
            while(escribiendo)
            {
                ++nle;
                try
                {
                    lector.await();
                }catch(Exception e){}
                --nle;
            }
            nl += 1;
            lector.signal();
        }
        finally
        {
            l.unlock();
        }
    }

    public void cerrarLectura()
    {
        l.lock();
        try
        {
            nl -= 1;
            if(nl == 0)
            {
                escritor.signal();
            }
        }finally
        {
            l.unlock();
        }
    }

    public void abrirEscritura()
    {
        l.lock();
        try{
            while(nl > 0 || escribiendo)
            {
                try
                {
                    escritor.await();
                }catch(Exception e){}
            }
            escribiendo = true;
        }finally
        {
            l.unlock();
        }
    }

    public void cerrarEscritura()
    {
        l.lock();
        try
        {
            escribiendo = false;
            if(nle > 0)
            {
                lector.signal();
            }
            else
            {
                escritor.signal();
            }
        }finally
        {
            l.unlock();
        }
    }
}
