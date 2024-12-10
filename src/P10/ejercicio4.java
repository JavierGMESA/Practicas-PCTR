package P10;
import mpi.*;
public class ejercicio4 
{
    public static void main(String args[]) throws Exception 
    {
        MPI.Init(args);
        int master = 0;
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        
        if(me == master)
        {
            Master(size);
        }
        else
        {
            Slave(me, size);
        }
        MPI.Finalize();
    }

    public static void Master(int size)
    {
        double[] v;
        v = new double[size - 1];
        double h = 1.0 / (size - 1);
        for(int i = 0; i < size - 1; ++i)
        {
            v[i] = i * h;
        }
        MPI.COMM_WORLD.Bcast(v, 0, size-1, MPI.DOUBLE, 0);
        double[] res = new double[1];
        res[0] = 0.0;
        double[] dev = new double[1];
        MPI.COMM_WORLD.Reduce(res, 0, dev, 0, 1, MPI.DOUBLE, MPI.SUM, 0);
        System.out.println("El resultado de la integral es " + dev[0]);

    }

    public static void Slave(int id, int size)
    {
        double[] v;
        v = new double[size-1];
        MPI.COMM_WORLD.Bcast(v, 0, size-1, MPI.DOUBLE, 0);
        double h = 1.0 / (size - 1);
        double rangoIni = v[id - 1];
        double punto_medio = rangoIni + h / 2;
        double[] res = new double[1];
        res[0] = f(punto_medio) * h;
        double[] dev = new double[1];
        MPI.COMM_WORLD.Reduce(res, 0, dev, 0, 1, MPI.DOUBLE, MPI.SUM, 0);
    }

    public static double f(double x)
    {
        return (4.0 / (1 + x * x));
    }
}
