package P10;
import mpi.*;
public class ejercicio2 
{
    public static void main(String args[]) throws Exception 
    {
        MPI.Init(args);
        int master = 0, slave = 1;
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
        for(int i = 0; i < 10; i++)
        {
            v[i] = i + 1;
        }
        MPI.COMM_WORLD.Bcast(v, 0, 10, MPI.INT, 0);
    }

    public static void Slave(int id)
    {
        int[] v;
        v = new int[10];
        MPI.COMM_WORLD.Bcast(v, 0, 10, MPI.INT, 0);
        for(int i = 0; i < 10; ++i)
        {
            v[i] = v[i] * id;
        }
        System.out.println("El vector con id " + id +  " tiene la forma [" + v[0] + ", " + v[1] + ", ... , " + v[9] + "]");
    }
}
