package P9;

import java.util.concurrent.locks.*;

public class PCMonitorAltoNivel 
{
    final int N;
	int Oldest = 0, Newest = 0;
  	volatile int Count = 0;
	int[] Buffer;
    ReentrantLock l;
    Condition vacio, lleno;

	public PCMonitorAltoNivel(int tam)
	{
		N = tam;
		Buffer = new int[tam];
        l = new ReentrantLock();
        vacio = l.newCondition();
        lleno = l.newCondition();
	}

	void Append(int V) 
    {
        l.lock();
        try
        {
            while (Count == N)
                try
                {
                    lleno.await();
                }catch (InterruptedException e) {}
	        Buffer[Newest] = V;
	        Newest = (Newest + 1) % N;
	        Count = Count + 1;
            vacio.signal();
        }finally
        {
            l.unlock();
        }
	}

	int Take() 
    {
        l.lock();
        try
        {
            int temp;
            while (Count == 0)
                try {
                    vacio.await();
                } catch (InterruptedException e) {}
	        temp = Buffer[Oldest];
	        Oldest = (Oldest + 1) % N;
	        Count = Count - 1;
	        lleno.signal();
            return temp;
        }finally
        {
            l.unlock();
        }
	}
}
