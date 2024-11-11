package P6;

public class tADListaSegura {

    private int[] lista;
    private int tamaño_max, tamaño;

    public tADListaSegura(int tama)
    {
        tamaño_max = tama;
        lista = new int[tama];
        tamaño = 0;
    }

    synchronized public void insertarComienzo(int elem)
    {
        if(tamaño < tamaño_max)
        {
            for(int i = tamaño; i > 0; --i)
            {
                lista[i] = lista[i - 1];
            }
            lista[0] = elem;
            ++tamaño;
        }
        mostrar();
    }

    synchronized public void eliminarComienzo()
    {
        if(tamaño != 0)
        {
            for(int i = 0; i < tamaño - 1; ++i)
            {
                lista[i] = lista[i + 1];
            }
            --tamaño;
        }
        mostrar();
    }

    synchronized public void insertarFinal(int elem)
    {
        if(tamaño < tamaño_max)
        {
            lista[tamaño] = elem;
            ++tamaño;
        }
        mostrar();
    }

    synchronized public void eliminarFinal()
    {
        if(tamaño != 0)
        {
            --tamaño;
        }
        mostrar();
    }

    public void mostrar()
    {
        System.out.print("[");
        for(int i = 0; i < tamaño; ++i)
        {
            if(i != tamaño - 1)
            {
                System.out.print(lista[i] + ", ");
            }
            else
            {
                System.out.print(lista[i]);
            }
        }
        System.out.println("]");
    }
}
