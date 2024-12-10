package P10;
import mpi.*;
public class ejercicio5 
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
        int[] v = new int[size];
        for(int i = 0; i < size; ++i)
        {
            v[i] = 0;
        }
        int[] op = new int[1];
        int[] res = new int[size];
        MPI.COMM_WORLD.Scatter(v, 0, 1, MPI.INT, op, 0, 1, MPI.INT, 0);     //IMPORTANTE: EN EL Gather Y EN EL Scatter, SI EL VECTOR A ENVIAR/DONDE GUARDAR ES DE 10 ELEMENTOS
        MPI.COMM_WORLD.Gather(op, 0, 1, MPI.INT, res, 0, 1, MPI.INT, 0);    //Y QUEREMOS QUE CADA PROCESO RECIBA 1 ELEMENTO, NO PONEMOS TAMAÑO 10, SI NO 1
        System.out.print("El vector resultante es [");
        for(int i = 0; i < size - 1; ++i)
        {
            System.out.print(res[i] + ", ");
        }
        System.out.println(res[size - 1] + "]");
    }

    public static void Slave(int id, int size)
    {
        int[] v = new int[size];
        int[] op = new int[1];
        MPI.COMM_WORLD.Scatter(v, 0, 1, MPI.INT, op, 0, 1, MPI.INT, 0);
        op[0] += id;
        MPI.COMM_WORLD.Gather(op, 0, 1, MPI.INT, v, 0, 1, MPI.INT, 0);
    }
}
