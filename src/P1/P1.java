package P1;

import java.util.Random; //random.nextTipo(valor_máximo - 1) no es útil con hilos pues no está explicitamente sincronizado aunque sea no estático 
                         //y puede repetirse la semilla
import java.util.concurrent.ThreadLocalRandom; //ThreadLocalRandom.current().nextTipo(valor_máximo - 1) Es útil con hilos pues usa una instancia 
                                               //diferente por cada hilo. Genera entre 0 y el valor máximo - 1
//Está la tercera opción Math.Random() que funciona igual que Random() pero generando numeron aleatorios double entre 0.0 y 1.0 y con sincronización

import java.lang.Thread;

public class P1
{
    public void prueba_random()
    {
        int na1, na2, na3;
        Random random = new Random();
        na1 = random.nextInt(100);
        na2 = ThreadLocalRandom.current().nextInt(100);
        na3 = (int) (Math.random() * 100);
    }

    public void ej2() throws InterruptedException
    {
        ej2f1 e1 = new ej2f1();
        ej2f2 e2 = new ej2f2();
        Thread h1 = new Thread(e1);
        Thread h2 = new Thread(e2);
        h1.start();
        h2.start();
        h1.join();
        h2.join();
    }

    public void ej3() throws InterruptedException
    {
        double res1 = 0;
        double res2 = 0;
        double[] v1 = new double[1000];
        double[] v2 = new double[1000];
        double[] v3 = new double[1000];
        double[] v4 = new double[1000];
        ej3f1 e1 = new ej3f1(v1);
        ej3f1 e2 = new ej3f1(v2);
        ej3f1 e3 = new ej3f1(v3);
        ej3f1 e4 = new ej3f1(v4);
        Thread h1 = new Thread(e1);
        Thread h2 = new Thread(e2);
        Thread h3 = new Thread(e3);
        Thread h4 = new Thread(e4);
        h1.start();
        h2.start();
        h3.start();
        h4.start();
        h1.join();
        h2.join();
        h3.join();
        h4.join();
        res1 = (sumavect(v1) + sumavect(v2) + sumavect(v3) + sumavect(v4)) / 4000;
        System.out.println("El area de la primera función es " + res1);
        ej3f2 e5 = new ej3f2(v1);
        ej3f2 e6 = new ej3f2(v2);
        ej3f2 e7 = new ej3f2(v3);
        ej3f2 e8 = new ej3f2(v4);
        h1 = new Thread(e5);
        h2 = new Thread(e6);
        h3 = new Thread(e7);
        h4 = new Thread(e8);
        h1.start();
        h2.start();
        h3.start();
        h4.start();
        h1.join();
        h2.join();
        h3.join();
        h4.join();
        res2 = (sumavect(v1) + sumavect(v2) + sumavect(v3) + sumavect(v4)) / 4000;
        System.out.println("El area de la segunda función es " + res2);
    }

    public double sumavect(double[] v)
    {
        double res = 0;
        for(int i = 0; i < v.length; ++i)
        {
            res += v[i];
        }
        return res;
    }
}

class ej2f1 implements Runnable
{
    public void run()
    {
        Boolean cero = false;
        double x1, x0;
        x0 = ThreadLocalRandom.current().nextDouble(2);
        x1 = 4;
        int iter = 0;
        while(iter < 100)
        {
            System.out.println("El valor actual de Xi en la función 1 es " + x0);
            if(fp(x0) != 0)
            {
                x1 = x0 - (f(x0) / fp(x0));
            }
            else
            {
                cero = !cero;
            }
            x0 = x1;
            iter++;
        }
    }

    private double f(double x)
    {
        return Math.cos(x) - (x * x * x);
    }

    private double fp(double x)
    {
        return -(Math.sin(x)) - 3 * (x * x);
    }
}

class ej2f2 implements Runnable
{
    public void run()
    {
        Boolean cero = false;
        double x1, x0;
        x0 = ThreadLocalRandom.current().nextDouble(2) + 2;
        x1 = 4;
        int iter = 0;
        while(!cero && iter < 10000)
        {
            System.out.println("El valor actual de Xi en la función 2 es " + x0);
            if(fp(x0) != 0)
            {
                x1 = x0 - (f(x0) / fp(x0));
            }
            else
            {
                cero = !cero;
            }
            x0 = x1;
            iter++;
        }
    }

    private double f(double x)
    {
        return x * x - 5;
    }

    private double fp(double x)
    {
        return 2 * x;
    }
}

class ej3f1 implements Runnable
{
    private double[] valores;

    public ej3f1(double[] num)
    {
        valores = num;
    }

    public void run()
    {
        for(int i = 0; i < valores.length; ++i)
        {
            valores[i] = f(ThreadLocalRandom.current().nextDouble());
        }
    }

    public double f(double x)
    {
        return Math.sin(x);
    }
}

class ej3f2 implements Runnable
{
    private double[] valores;

    public ej3f2(double[] num)
    {
        valores = num;
    }

    public void run()
    {
        for(int i = 0; i < valores.length; ++i)
        {
            valores[i] = f(ThreadLocalRandom.current().nextDouble());
        }
    }

    public double f(double x)
    {
        return x;
    }
}