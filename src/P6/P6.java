package P6;

import java.net.ServerSocket;
import java.net.Socket;

import java.io.*; //IMPORTANTE: PARA IOException, OutputStream, InputStream, BufferedReader, InputStreamReader Y PrintWriter
import java.util.concurrent.*;

import P1.*;

public class P6 
{
    public void ejercicio1() throws IOException
    {
        //LADO DEL SERVIDOR
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket = serverSocket.accept(); // Espera una conexión de un cliente
        OutputStream outClient = clientSocket.getOutputStream();
        InputStream inClient = clientSocket.getInputStream();

        //LADO DEL CLIENTE
        Socket socket = new Socket("localhost", 8080); // Se conecta al servidor en localhost en el puerto 8080
        OutputStream outServer = socket.getOutputStream(); //OutputStream para escribir en el lado del servidor
        InputStream inServer = socket.getInputStream(); //InputStream para leer del lado del servidor

        // Para intercambiar mensajes simplemente el emisor escribe en su OutputStream mientras que el
        // receptor lee de un InputStream. La forma de utilizar estos InputStream y OutputStream es la
        // misma que con System.in y System.out:

        //LEYENDO
        BufferedReader reader = new BufferedReader(new InputStreamReader(inClient));
        String messageFromClient = reader.readLine();
        System.out.println("Mensaje del cliente: " + messageFromClient);

        //ESCRIBIENDO
        PrintWriter writer = new PrintWriter(outClient, true);
        writer.println("Mensaje recibido: " + messageFromClient);

    }

    public void ejercicio4()
    {
        tADListaSegura lista = new tADListaSegura(60);
        ThreadPoolExecutor tp = new ThreadPoolExecutor(10, 40, 90000000, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        for(int i = 0; i < 10; ++i)
        {
            tp.execute(new usaTADListaSegura(lista));
        }
        tp.shutdown();
    }

    public void ejercicio5() throws InterruptedException
    {
        heterogenea h = new heterogenea();
        ThreadPoolExecutor tp = new ThreadPoolExecutor(1000, 5000, 90000000, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        for(int i = 0; i < 1000; ++i)
        {
            tp.execute(new usaheterogenea(h));
        }
        tp.awaitTermination(1000, TimeUnit.MILLISECONDS);
        tp.shutdown();
        h.mostrarN();
        h.mostrarM();
    }

    public void ejercicio7() throws Exception
    {
        MonteCarloSecuencial mcs = new MonteCarloSecuencial(4000);
        ThreadPoolExecutor tp = new ThreadPoolExecutor(10, 30, 90000000, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        long tiempIni, tiempFin;
        double res;
        tiempIni = System.nanoTime();
        res = mcs.resultado();
        tiempFin = System.nanoTime();
        System.out.println("El tiempo secuencial es " + (tiempFin - tiempIni));

        tiempIni = System.nanoTime();
        Future<Double> f1 = tp.submit(new MonteCarloParalelo(1000));
        Future<Double> f2 = tp.submit(new MonteCarloParalelo(1000));
        Future<Double> f3 = tp.submit(new MonteCarloParalelo(1000));
        Future<Double> f4 = tp.submit(new MonteCarloParalelo(1000));
        tp.shutdown();
        res = (f1.get() + f2.get() + f3.get() + f4.get()) / 4000;
        tiempFin = System.nanoTime();
        System.out.println("El tiempo paralelo es " + (tiempFin - tiempIni));
    }
}

class usaTADListaSegura implements Runnable
{
    private int op;
    tADListaSegura lista;

    public usaTADListaSegura(tADListaSegura lis)
    {
        lista = lis;
        op = ThreadLocalRandom.current().nextInt() % 4;
    }

    public void run()
    {
        for(int i = 0; i < 4; ++i)
        {
            int elem = ThreadLocalRandom.current().nextInt() % 200;
            switch(op)
            {
                case 0: lista.insertarComienzo(elem); break;
                case 1: lista.eliminarComienzo(); break;
                case 2: lista.insertarFinal(elem); break;
                case 3: lista.eliminarFinal(); break;
            }
        }
    }
}

class usaheterogenea implements Runnable
{
    private heterogenea hete;

    public usaheterogenea(heterogenea h)
    {
        hete = h;
    }

    public void run()
    {
        hete.incN();
        hete.incM();
    }
}

class MonteCarloSecuencial
{
    private int tama;
    public MonteCarloSecuencial(int tama)
    {
        this.tama = tama;
    }

    public double resultado()
    {
        double res = 0;
        for(int i = 0; i < tama; ++i)
        {
            res+= f(ThreadLocalRandom.current().nextDouble());
        }
        res /= tama;
        return res;
    }

    private double f(double x)
    {
        return Math.cos(x);
    }
}

class MonteCarloParalelo implements Callable<Double>
{
    private int tama;

    public MonteCarloParalelo(int tama)
    {
        this.tama = tama;
    }

    public Double call()
    {
        double[] valores = new double[tama];
        for(int i = 0; i < valores.length; ++i)
        {
            valores[i] = f(ThreadLocalRandom.current().nextDouble());
        }
        return sumavect(valores);
    }

    private double f(double x)
    {
        return Math.cos(x);
    }

    private double sumavect(double[] v)
    {
        double res = 0;
        for(int i = 0; i < tama; ++i)
        {
            res += v[i];
        }
        return res;
    }
}
