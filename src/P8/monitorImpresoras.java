package P8;

public class monitorImpresoras 
{
    private int total;
    private int disponibles;
    private boolean[] impresoras;

    public monitorImpresoras(int n_impresoras)
    {
        total = n_impresoras;
        disponibles = total;
        impresoras = new boolean[total];
        for(int i = 0; i < total; ++i)
        {
            impresoras[i] = true;
        }
    }

    synchronized public int pedirImpresora()
    {
        try
        {
            while(disponibles == 0)
            {
                wait();
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
    }

    synchronized public void liberarImpresora(int impresora)
    {
        impresoras[impresora] = true;
        ++disponibles;
        notifyAll();
    }
}
