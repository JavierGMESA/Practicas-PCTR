package P6;

public class heterogenea 
{
    private int n = 0, m = 0;

    synchronized public void incN()
    {
        ++n;
    }

    public void incM()
    {
        ++m;
    }

    synchronized public void mostrarN()
    {
        System.out.println("El valor de n es " + n);
    }

    public void mostrarM()
    {
        System.out.println("El valor de m es " + m);
    }
}
