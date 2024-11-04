package P5;

import java.util.concurrent.*;
import java.math.BigInteger;

public class P5 
{
    public void ej1()
    {
        ExecutorService ejecutor = Executors.newFixedThreadPool(2);
        
        Callable<Integer> tarea = () -> {
            Thread.yield(); 
            return 42;
        };

        Future<Integer> resultado = ejecutor.submit(tarea);

        try{
            Integer valor = resultado.get(); //IMPORTANTE: EL MÉTODO get DE Future ES BLOQUEANTE. ESTE PUEDE LANZAR UN ExecutionException
            System.out.println("Resultado de la tarea: " + valor);
        }catch(InterruptedException | ExecutionException e){

        }
        ejecutor.shutdown();                //IMPORTANTE: RECORDAR APAGAR EL ThreadPool
    }

    public void ej3()
    {
        double[][] m = new double[1000][1000];
        double[][] v = new double[1000][1000];
        for(int i = 0; i < 1000; ++i)
        {
            for(int j = 0;  j < 1000; ++j)
            {
                v[i][j] = ThreadLocalRandom.current().nextDouble();
                m[i][j] = ThreadLocalRandom.current().nextDouble();
            }
        }

        //Secuencial
        matVector.prodMatVectSec(m, v);

        //Paralelo
        int num_hilos = 24;
        int inicio = 0;
        int aumento = 1000 / num_hilos;
        long tiempIni, tiempFin;
        tiempIni = System.nanoTime();
        Thread[] Pt = new Thread[num_hilos];
        for(int i = 0; i < num_hilos; ++i)
        {
            Pt[i] = new Thread(new matMatrizConcurrente(inicio, inicio + aumento, m, v));
            inicio += aumento;
        }
        for(int i = 0; i < num_hilos; ++i)
        {
            Pt[i].start();
        }
        for(int i = 0; i < num_hilos; ++i)
        {
            try
            {
                Pt[i].join();
            }catch(InterruptedException e){}
        }
        tiempFin = System.nanoTime();
        System.out.println("El tiempo que tarda el algoritmo paralelo es   " + (tiempFin - tiempIni) + "\n");
    }

    public void ej5()
    {
        double[][] m = new double[1000][1000];
        for(int i = 0; i < 1000; ++i)
        {
            for(int j = 0;  j < 1000; ++j)
            {
                m[i][j] = ThreadLocalRandom.current().nextDouble() % 256;
            }
        }

        //Secuencial
        ResaltoSecuencial.ResSecuencial(m);

        //Paralelo
        int num_hilos = 120;
        int inicio = 0;
        int aumento = 1000 / num_hilos;
        long tiempIni, tiempFin;
        tiempIni = System.nanoTime();
        Thread[] Pt = new Thread[num_hilos];
        for(int i = 0; i < num_hilos; ++i)
        {
            Pt[i] = new Thread(new ResaltoConcurrente(inicio, inicio + aumento, m));
            inicio += aumento;
        }
        for(int i = 0; i < num_hilos; ++i)
        {
            Pt[i].start();
        }
        for(int i = 0; i < num_hilos; ++i)
        {
            try
            {
                Pt[i].join();
            }catch(InterruptedException e){}
        }
        tiempFin = System.nanoTime();
        System.out.println("El tiempo que tarda el algoritmo paralelo es   " + (tiempFin - tiempIni) + "\n");
    }

    public void ej6()
    {
        BigInteger a = new BigInteger("1000000000000000000");
        BigInteger b = new BigInteger("2000000000000000000");

        BigInteger suma = a.add(b);
        BigInteger resta = a.subtract(b);
        BigInteger multiplicacion = a.multiply(b);
        BigInteger division = b.divide(a);
        
        System.out.println("Suma: " + suma);
        System.out.println("Resta: " + resta);
        System.out.println("Multiplicación: " + multiplicacion);
        System.out.println("División: " + division);

        BigInteger base = new BigInteger("3");
        int exponente = 5;
        BigInteger potencia = base.pow(exponente); // 3^5 = 243
        System.out.println("Potencia: " + potencia);
    }

    public void ej7()
    {
        BigInteger numero = new BigInteger("10000");
        ExecutorService ejecutor = Executors.newFixedThreadPool(5);

        //Ejecución secuencial
        FactorialSecuencial.FactSecuencial(numero);

        //Ejecución paralela
        long tiempIni, tiempFin;
        BigInteger res = new BigInteger("1");

        tiempIni = System.nanoTime();
        Future<BigInteger> h1 = ejecutor.submit(new FactorialParalelo(0, 2000));
        Future<BigInteger> h2 = ejecutor.submit(new FactorialParalelo(2000, 4000));
        Future<BigInteger> h3 = ejecutor.submit(new FactorialParalelo(4000, 6000));
        Future<BigInteger> h4 = ejecutor.submit(new FactorialParalelo(6000, 8000));
        Future<BigInteger> h5 = ejecutor.submit(new FactorialParalelo(8000, 10000));
        try
        {
            res.multiply(h1.get());
            res.multiply(h2.get());
            res.multiply(h3.get());
            res.multiply(h4.get());
            res.multiply(h5.get());
        }catch(InterruptedException | ExecutionException e){}
        ejecutor.shutdown();
        tiempFin = System.nanoTime();
        System.out.println("El tiempo que tarda el algoritmo paralelo es   " + (tiempFin - tiempIni) + "\n");
    }
}



class matVector
{
    static public void prodMatVectSec(double[][] m, double[][] v)
    {
        long tiempIni, tiempFin;
        int tam = v.length;
        double[][] res = new double[tam][tam];
        tiempIni = System.nanoTime();
        for(int i = 0; i < tam; ++i)
        {
            for(int j = 0; j < tam; ++j)
            {
                res[i][j] = 0;
                for(int k = 0; k < tam; ++k)
                {
                    res[i][j] += m[i][k] * v[k][j];
                }
            }
        }
        tiempFin = System.nanoTime();
        System.out.println("El tiempo que tarda el algoritmo secuencial es " + (tiempFin - tiempIni) + "\n");
    }
}

class matMatrizConcurrente implements Runnable
{
    private double[][] res = new double[1000][1000];
    private double[][] m;
    private double[][] v;
    private int inicio;
    private int fin;

    public matMatrizConcurrente(int inicio, int fin, double[][] m, double[][] v)
    {
        this.inicio = inicio;
        this.fin = fin;
        this.m = m;
        this.v = v;
    }

    public void run()
    {
        for(int i = inicio; i < fin; ++i)
        {
            for(int j = 0; j < 1000; ++j)
            {
                res[i][j] = 0;
                for(int k = 0; k < 1000; ++k)
                {
                    res[i][j] += m[i][k] * v[k][j];
                }
            }
        }
        //System.out.println("Fin desde la fila " + inicio + " hasta la fila " + fin + "\n");
    }
}

class ResaltoSecuencial
{
    static public void ResSecuencial(double[][] m)
    {
        long tiempIni, tiempFin;
        int tam = 1000;
        double[][] res = new double[tam][tam];
        tiempIni = System.nanoTime();
        for(int i = 0; i < tam; ++i)
        {
            for(int j = 0; j < tam; ++j)
            {
                if(!(i == 0 || i == tam - 1 || j == 0 || j ==tam - 1))
                {
                    res[i][j] = (4 * m[i][j] - m[i + 1][j] - m[i][j + 1] - m[i- 1][j] - m[i][j - 1]) / 8;
                }
                else
                {
                    res[i][j] = m[i][j];
                }
            }
        }
        tiempFin = System.nanoTime();
        System.out.println("El tiempo que tarda el algoritmo secuencial es " + (tiempFin - tiempIni) + "\n");
    }
}

class ResaltoConcurrente implements Runnable
{
    private double[][] res = new double[1000][1000];
    private double[][] m;
    private int inicio;
    private int fin;

    public ResaltoConcurrente(int inicio, int fin, double[][] m)
    {
        this.inicio = inicio;
        this.fin = fin;
        this.m = m;
    }

    public void run()
    {
        for(int i = inicio; i < fin; ++i)
        {
            for(int j = 0; j < 1000; ++j)
            {
                if(!(i == 0 || i == 999 || j == 0 || j == 999))
                {
                    res[i][j] = (4 * m[i][j] - m[i + 1][j] - m[i][j + 1] - m[i- 1][j] - m[i][j - 1]) / 8;
                }
                else
                {
                    res[i][j] = m[i][j];
                }
            }
        }
        //System.out.println("Fin desde la fila " + inicio + " hasta la fila " + fin + "\n");
    }
}

class FactorialSecuencial
{
    public static void FactSecuencial(BigInteger a)
    {
        long tiempIni, tiempFin;
        BigInteger res = new BigInteger("1");
        BigInteger i = new BigInteger("1");
        tiempIni = System.nanoTime();
        while(i.compareTo(a) < 1)
        {
            res = res.multiply(i);
            i = i.add(new BigInteger("1"));
        }
        tiempFin = System.nanoTime();
        System.out.println("El tiempo que tarda el algoritmo secuencial es " + (tiempFin - tiempIni) + "\n");
    }
}

class FactorialParalelo implements Callable<BigInteger>
{
    private BigInteger inicio;
    private BigInteger fin;

    public FactorialParalelo(int inicio, int fin)
    {
        this.inicio = new BigInteger((inicio + 1) + "");
        this.fin = new BigInteger(fin + "");
    }

    @Override
    public BigInteger call()
    {
        BigInteger res = new BigInteger("1");
        BigInteger i = inicio;
        while(i.compareTo(fin) < 1)
        {
            res = res.multiply(i);
            i = i.add(new BigInteger("1"));
        }
        return res;
    }
}


