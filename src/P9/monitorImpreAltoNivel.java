package P9;

import java.util.concurrent.locks.*;

public class monitorImpreAltoNivel 
{
    private int total;
    private int disponibles;
    private boolean[] impresoras;
    private ReentrantLock l;
    private Condition cogidas;

    public monitorImpreAltoNivel(int n_impresoras)
    {
        total = n_impresoras;
        disponibles = total;
        impresoras = new boolean[total];
        for(int i = 0; i < total; ++i)
        {
            impresoras[i] = true;
        }
        l = new ReentrantLock();
        cogidas = l.newCondition();
    }

    public int pedirImpresora()
    {
        l.lock();
        try
        {
            try
            {
                while(disponibles == 0)
                {
                    cogidas.await();
                }
            }catch(Exception e){}
            int impresora = -1;
            int i = 0;
            while(i < total && impresora == -1)
            {
                if(impresoras[i] == true)
                {
                    impresora = i;
                    impresoras[i] = false;
                }
                ++i;
            }
            --disponibles;
            return impresora;
        }finally
        {
            l.unlock();
        }
    }

    public void liberarImpresora(int impresora)
    {
        l.lock();
        try
        {
            impresoras[impresora] = true;
            ++disponibles;
            cogidas.signal();
        }finally
        {
            l.unlock();
        }
    }
}
