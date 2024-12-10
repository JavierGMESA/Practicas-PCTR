package P10;
import mpi.*;
public class ejercicio3 
{
    public static void main(String args[]) throws Exception 
    {
        MPI.Init(args);
        int master = 0;
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        
        if(me == master)
        {
            Master();
        }
        else
        {
            Slave(me);
        }
        MPI.Finalize();
    }

    public static void Master()
    {
        int[] v;
        v = new int[10];
        for(int i = 0; i < 10; ++i)
        {
            v[i] = i * 1000000;
        }
        MPI.COMM_WORLD.Bcast(v, 0, 10, MPI.INT, 0);
        int[] res = new int[1];
        res[0] = 0;
        int[] dev = new int[1];
        MPI.COMM_WORLD.Reduce(res, 0, dev, 0, 1, MPI.INT, MPI.SUM, 0);
        System.out.println("El número de primos encontrados es " + dev[0]);

    }

    public static void Slave(int id)
    {
        int[] v;
        v = new int[10];
        MPI.COMM_WORLD.Bcast(v, 0, 10, MPI.INT, 0);
        int rangoIni = v[id - 1];
        int rangoFin = rangoIni + 1000000;
        int numPrimos = 0;
        boolean no_primo;
        for(int i = rangoIni + 1; i <= rangoFin; ++i)
        {
            int j = 2;
            no_primo = false;
            while(j < Math.sqrt(i) && !no_primo)
            {
                if(i % j == 0)
                {
                    no_primo = true;
                }
                ++j;
            }
            if(!no_primo && i >= 2)
            {
                ++numPrimos;
            }   
        }
        int[] res = new int[1];
        res[0] = numPrimos;
        int[] dev = new int[1];
        MPI.COMM_WORLD.Reduce(res, 0, dev, 0, 1, MPI.INT, MPI.SUM, 0);
    }
}
