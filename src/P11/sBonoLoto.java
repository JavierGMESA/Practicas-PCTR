package P11;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.concurrent.ThreadLocalRandom;

public class sBonoLoto extends UnicastRemoteObject implements iBonoLoto
{
    int[] ganador;

    public sBonoLoto() throws RemoteException
    {
        ganador = new int[6];
        for(int i = 0; i < 6; ++i)
        {
            ganador[i] = ThreadLocalRandom.current().nextInt() % 49 + 1;
        }
    }

    public void resetServidor() throws RemoteException
    {
        for(int i = 0; i < 6; ++i)
        {
            ganador[i] = ThreadLocalRandom.current().nextInt() % 49 + 1;
        }
    }
    public boolean compApuesta(int[] apuesta)  throws RemoteException
    {
        boolean acertado = true;
        int i = 0;
        while (i < 6 && acertado)
        {
            acertado = acertado && apuesta[i] == ganador[i];
        }
        return acertado;
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
        sBonoLoto obj = new sBonoLoto();
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
