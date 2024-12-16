package P11;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class sPiMonteCarlo extends UnicastRemoteObject implements iPiMonteCarlo
{
    private int totalPuntos;
    private int puntosDentroDelCirculo;

    public sPiMonteCarlo() throws RemoteException
    {
        totalPuntos = puntosDentroDelCirculo = 0;
    }

    public synchronized void reset() throws RemoteException         //IMPORTANTE: HAY QUE HACERLOS synchronized PARA QUE NO OCURRAN CONDICIONES DE CARRERA
    {
        totalPuntos = puntosDentroDelCirculo = 0;
    }
    public synchronized void masPuntos(int nPuntos)  throws RemoteException
    {
        totalPuntos += nPuntos;
        for(int i = 0; i < nPuntos; ++i)
        {
            double x = ThreadLocalRandom.current().nextDouble();
            double y = ThreadLocalRandom.current().nextDouble();
            if(x * x + y * y <= 1)
            {
                puntosDentroDelCirculo++;
            }
        }
    }
    public synchronized double aproxActual() throws RemoteException
    {
        return 4.0 * puntosDentroDelCirculo / totalPuntos;
    }

    private static void arrancarRegistro(int numPuerto) throws RemoteException
    {
        try
        {
            Registry res = LocateRegistry.getRegistry(numPuerto);
            res.list();
        }catch(Exception e)
        {
            Registry res = LocateRegistry.createRegistry(numPuerto);
        }
    }
    
    public static void main(String[] args) throws RemoteException
    {
        sPiMonteCarlo obj = new sPiMonteCarlo();
        int NumPuerto = 8080;
        arrancarRegistro(NumPuerto);
        String URLRegistro = "rmi://localhost:" + NumPuerto + "/bonoloto";
        try
        {
            Naming.rebind(URLRegistro, obj);
        }catch(MalformedURLException e)
        {
            System.out.println("Error con la url");
        }
        System.out.println("Servidor preparado");
    }
}
