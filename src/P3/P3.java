package P3;
import java.lang.Thread;
import java.lang.InterruptedException;
import java.util.concurrent.*;
import java.util.Arrays; //Para Arrays.sort()
import java.util.Random; //Para random

public class P3 
{
    public void ejercicio1()
    {
        // Generar un vector de 1000 números reales aleatorios
        double[] vector = new double[1000];
        //Random random = new Random();
        
        for (int i = 0; i < vector.length; i++) {
            //vector[i] = random.nextDouble() * 1000; // Números aleatorios entre 0 y 1000. ES MEJORABLE
            vector[i] = ThreadLocalRandom.current().nextDouble() * 1000; //Mejor por el tema de uso entre hilos
        }
        
        // Medir el tiempo de inicio
        long startTime = System.nanoTime();
        
        // Ordenar el vector
        Arrays.sort(vector); //ES UN ALGORITMO DE RÁPIDA ORDENACIÓN (Dual-Pivot Quicksort)
        
        // Medir el tiempo de finalización
        long endTime = System.nanoTime();
        
        // Calcular la duración
        long duration = endTime - startTime;
        
        // Mostrar el tiempo de ejecución en nanosegundos
        System.out.println("Tiempo de ejecución para ordenar el vector: " + duration + " nanosegundos");
    }

    public void ejercicio2() throws InterruptedException
    {
        //Secuencial
        double[] v1, v2;
        v1 = new double[1000000];
        v2 = new double[1000000];
        int tam = v1.length;
        for(int i = 0; i < tam; ++i)
        {
            v1[i] = ThreadLocalRandom.current().nextDouble();
            v2[i] = ThreadLocalRandom.current().nextDouble();
        }
        prodEscalar.prodEscalarSec(v1, v2);

        //Paralelo
        prodEscalarParalelo h1, h2, h3, h4, h5, h6, h7, h8;
        long tiempIni, tiempFin;
        double res = 0;
        double[] parcial;
        tiempIni = System.nanoTime();
        //h1 = new prodEscalarParalelo(0, 0, 166666, v1, v2);
        //h2 = new prodEscalarParalelo(1, 166666, 333332, v1, v2);
        //h3 = new prodEscalarParalelo(2, 333332, 500000, v1, v2);
        //h4 = new prodEscalarParalelo(3, 500000, 666666, v1, v2);
        //h5 = new prodEscalarParalelo(4, 666666, 833332, v1, v2);
        //h6 = new prodEscalarParalelo(5, 833332, 1000000, v1, v2);
        h1 = new prodEscalarParalelo(0, 0, 125000, v1, v2);
        h2 = new prodEscalarParalelo(1, 125000, 250000, v1, v2);
        h3 = new prodEscalarParalelo(2, 250000, 375000, v1, v2);
        h4 = new prodEscalarParalelo(3, 375000, 500000, v1, v2);
        h5 = new prodEscalarParalelo(4, 500000, 625000, v1, v2);
        h6 = new prodEscalarParalelo(5, 625000, 750000, v1, v2);
        h7 = new prodEscalarParalelo(6, 750000, 875000, v1, v2);
        h8 = new prodEscalarParalelo(7, 875000, 1000000, v1, v2);
        h1.start();
        h2.start();
        h3.start();
        h4.start();
        h5.start();
        h6.start();
        h7.start();
        h8.start();
        h1.join();
        h2.join();
        h3.join();
        h4.join();
        h5.join();
        h6.join();
        h7.join();
        h8.join();
        parcial = prodEscalarParalelo.resultado();
        for(int i = 0; i < parcial.length; ++i)
        {
            res += parcial[i];
        }
        tiempFin = System.nanoTime();
        System.out.println("El tiempo que tarda el algoritmo paralelo es " + (tiempFin - tiempIni) + "\nY el resultado es " + res + "\n");
    }

    public void ejercicio3() throws InterruptedException
    {
        Random random = new Random();
        double[][] m = new double[1000][1000];
        double[] v = new double[1000];
        for(int i = 0; i < 1000; ++i)
        {
            v[i] = random.nextDouble();
            for(int j = 0;  j < 1000; ++j)
            {
                m[i][j] = random.nextDouble();
            }
        }

        //Secuencial
        matVector.prodMatVectSec(m, v);

        //Paralelo
        int num_hilos = 2;
        int inicio = 0;
        int aumento = 1000 / num_hilos;
        long tiempIni, tiempFin;
        tiempIni = System.nanoTime();
        Thread[] Pt = new Thread[num_hilos];
        for(int i = 0; i < num_hilos; ++i)
        {
            Pt[i] = new Thread(new matVectorConcurrente(inicio, inicio + aumento, m, v));
            inicio += aumento;
        }
        for(int i = 0; i < num_hilos; ++i)
        {
            Pt[i].start();
        }
        for(int i = 0; i < num_hilos; ++i)
        {
            Pt[i].join();
        }
        tiempFin = System.nanoTime();
        System.out.println("El tiempo que tarda el algoritmo paralelo es   " + (tiempFin - tiempIni) + "\n");
    }
}

class prodEscalar
{
    static public void prodEscalarSec(double[] v1, double[] v2)
    {
        long tiempIni, tiempFin;
        int tam = v1.length;
        double res = 0.0;
        tiempIni = System.nanoTime();
        for(int i = 0; i != tam; ++i)
        {
            res += v1[i] * v2[i];
        }
        tiempFin = System.nanoTime();
        System.out.println("El tiempo que tarda el algoritmo secuencial es " + (tiempFin - tiempIni) + "\nY el resultado es " + res + "\n");
    }
}

class prodEscalarParalelo extends Thread
{
    private static double[] productoParcial = new double[8];
    private int idHebra, inicio, fin;
    private double[] v1, v2;

    public prodEscalarParalelo(int idHebra, int inicio, int fin, double[] v1, double[] v2)
    {
        this.idHebra = idHebra;
        this.inicio = inicio;
        this.fin = fin;
        this.v1 = v1;
        this.v2 = v2;
    }

    public static double[] resultado()
    {
        return productoParcial;
    }

    public void start()
    {
        productoParcial[idHebra] = 0.0;
        for(int i = inicio; i < fin; ++i)
        {
            productoParcial[idHebra] += v1[i] * v2[i];
        }
    }
}

class matVector
{
    static public void prodMatVectSec(double[][] m, double[] v)
    {
        long tiempIni, tiempFin;
        int tam = v.length;
        double[] res = new double[tam];
        tiempIni = System.nanoTime();
        for(int i = 0; i < tam; ++i)
        {
            res[i] = 0;
            for(int j = 0; j < tam; ++j)
            {
                res[i] += m[i][j] * v[j];
            }
        }
        tiempFin = System.nanoTime();
        System.out.println("El tiempo que tarda el algoritmo secuencial es " + (tiempFin - tiempIni) + "\n");
    }
}

class matVectorConcurrente implements Runnable
{
    private double[] res = new double[1000];
    private double[][] m;
    private double[] v;
    private int inicio;
    private int fin;

    public matVectorConcurrente(int inicio, int fin, double[][] m, double[] v)
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
            res[i] = 0;
            for(int j = 0; j < 1000; ++j)
            {
                res[i] += m[i][j] * v[j];
            }
        }
        System.out.println("Fin desde la fila " + inicio + " hasta la fila " + fin + "\n");
    }
}
