package P7;

public class LEMonitor {
    boolean escribiendo;
    int nl;

    public LEMonitor()
    {
        escribiendo = false;
        nl = 0;
    }

    synchronized public void abrirLectura()
    {
        while(escribiendo)
        {
            try
            {
                wait();
            }catch(Exception e){}
        }
        nl += 1;
        notifyAll(); //Para avisar a otros lectores que pueden leer
    }

    synchronized public void cerrarLectura()
    {
        nl -= 1;
        if(nl == 0)
        {
            notifyAll(); //Para avisar a escritores que pueden escribir
        }
    }

    synchronized public void abrirEscritura()
    {
        while(nl > 0 || escribiendo)
        {
            try
            {
                wait();
            }catch(Exception e){}
        }
        escribiendo = true;
    }

    synchronized public void cerrarEscritura()
    {
        escribiendo = false;
        notifyAll(); //Para avisar tanto a lectores como otros escritores
    }
}
