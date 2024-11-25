package P8;

public class cuentaCompartida 
{
    public int saldo;
    
    public cuentaCompartida()
    {
        saldo = 0;
    }

    synchronized public void ingreso(int cuantia)
    {
        saldo += cuantia;
        notifyAll();
    }

    synchronized public void reintegro(int cuantia)
    {
        try
        {
            while(saldo < cuantia)
            {
                wait();
            }
        }catch(Exception e){}
        saldo -= cuantia;
    }
    
    public int valor()
    {
        return saldo;
    }
}

