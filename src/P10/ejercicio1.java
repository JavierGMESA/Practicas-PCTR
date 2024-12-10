package P10;
import mpi.*;
public class ejercicio1 
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
        else if(me == slave)
        {
            Slave();
        }
        MPI.Finalize();
    }
    
    public static void Master()
    {
        int[] v1, v2, v3;
        v1 = new int[4];
        v2 = new int[4];
        for(int i = 0; i < 4; i++)
        {
            v1[i] = i * 2;
            v2[i] = i * 3;
        }
        MPI.COMM_WORLD.Send(v1, 0, 4, MPI.INT, 1, 0);
        MPI.COMM_WORLD.Send(v2, 0, 4, MPI.INT, 1, 0);
        v3 = new int[1];
        Status est = MPI.COMM_WORLD.Recv(v3, 0, 1, MPI.INT, 1, 0);
        System.out.println("El valor del producto escalar es " + v3[0]);
    }

    public static void Slave()
    {
        int[] v1, v2, v3;       //IMPORTANTE: LOS VECTORES SE DECLARAN COMO int SIN MAS
        v1 = new int[4];        //IMPORTANTE: LOS VECTORES QUE SE MANDAN O SE RECIBEN DEBEN ESTAR INICIALIZADOS
        v2 = new int[4];
        Status est1 = MPI.COMM_WORLD.Recv(v1, 0, 4, MPI.INT, 0, 0);
        Status est2 = MPI.COMM_WORLD.Recv(v2, 0, 4, MPI.INT, 0, 0);
        v3 = new int[1];
        v3[0] = 0;
        for(int i = 0; i < 4; ++i)
        {
            v3[0] = v3[0] + v1[i] * v2[i];
        }
        MPI.COMM_WORLD.Send(v3, 0, 1, MPI.INT, 0, 0);
    }
}
