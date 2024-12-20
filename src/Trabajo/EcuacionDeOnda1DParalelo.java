package Trabajo;

import java.util.*;
import java.util.concurrent.*;

public class EcuacionDeOnda1DParalelo {
    public static void main(String[] args) throws InterruptedException {
        int N = 5000;
        int T_max = 10000;
        double v = 0.5;
        int numHilos = 32;

        UsaEcuacionDeOnda1DMonitor(N, T_max, v, numHilos);

        // PRUEBAS
        System.out.println("Pruebas:");
        // Ponemos un N negativo
        try
        {
            UsaEcuacionDeOnda1DMonitor(-1, T_max, v, numHilos); // Lanza excepción "N ha de ser un entero positivo"
        }catch(Exception e)
        {
            System.out.println("Excepción 1: " + e.getMessage());
        }
        
        //Ponemos un T_max negativo
        try
        {
            UsaEcuacionDeOnda1DMonitor(N, -1, v, numHilos); // Lanza excepción "T_max ha de ser un entero positivo"
        }catch(Exception e)
        {
            System.out.println("Excepción 2: " + e.getMessage());
        }
        
        // Ponemos un i fuera de rango
        try
        {
            EcuacionDeOnda1DMonitor ecuacion = new EcuacionDeOnda1DMonitor(10, 10, v);
            ecuacion.calcularCelda(-3, 8); // Lanza excepción "Primer parámetro fuera de rango"
        }catch(Exception e)
        {
            System.out.println("Excepción 3: " + e.getMessage());
        }
        
        // Ponemos un t fuera de rango
        try
        {
            EcuacionDeOnda1DMonitor ecuacion = new EcuacionDeOnda1DMonitor(10, 10, v);
            ecuacion.calcularCelda(3, -3); // Lanza excepción "Segundo parámetro fuera de rango"
        }catch(Exception e)
        {
            System.out.println("Excepción 4: " + e.getMessage());
        }
        
        // Ponemos un número de hilos menor que 1
        try
        {
            UsaEcuacionDeOnda1DMonitor(N, T_max, v, -1);
        }catch(Exception e)
        {
            System.out.println("Excepción 5: " + e.getMessage());
        }
        // Ponemos un número de hilos mayor que N + 1
        try
        {
            UsaEcuacionDeOnda1DMonitor(N, T_max, v, N + 2);
        }catch(Exception e)
        {
            System.out.println("Excepción 6: " + e.getMessage());
        }
    }

    public static void UsaEcuacionDeOnda1DMonitor(int N, int T_max, double v, int numHilos)
    {
        long tiempIni, tiempFin;

        EcuacionDeOnda1DMonitor ecuacion = new EcuacionDeOnda1DMonitor(N, T_max, v);

        // Crear un ThreadPoolExecutor
        if(numHilos < 1)
        {
            throw new IllegalArgumentException("El número de hilos debe ser mayor que 0");
        }
        if(numHilos > N + 1)
        {
            throw new IllegalArgumentException("El número de hilos debe ser menor que N + 1");
        }

        //for(int iteraciones = 0; iteraciones < 25; ++iteraciones){
        ExecutorService threadPool = Executors.newFixedThreadPool(numHilos);
        List<OndaRunnable> tareas = new ArrayList<>();
        
        tiempIni = System.nanoTime();
        // Dividir las tareas entre los hilos
        int rango = (N + 1) / numHilos;
        for (int h = 0; h < numHilos; h++) {
            int inicio = h * rango;
            int fin = (h == numHilos - 1) ? N : inicio + rango - 1;
            tareas.add(new OndaRunnable(ecuacion, inicio, fin, T_max));
        }

        // Enviar las tareas al ThreadPoolExecutor
        for (OndaRunnable tarea : tareas) {
            threadPool.execute(tarea);
        }

        // Se crean primero todas las tareas y luego se mandar al Thread Pool de cara a que haya el menor número de suspensiones de hilo

        // Cerrar el ThreadPoolExecutor
        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
            try
            {
            Thread.sleep(1); // Esperar a que todos los hilos terminen
            }catch(InterruptedException e)
            {
                System.out.println("Ha habido una excepción al dormir el hilo main");
            }
        }
        tiempFin = System.nanoTime();
        long tiempRes = (tiempFin - tiempIni) / 1000;
        long tiempNano = tiempFin - tiempIni;
        
        //}
        System.out.print("El tiempo del algoritmo paralelo con N=" + N + ", T_max=" + T_max + " y con " + numHilos + " hilos");
        System.out.println(" ha sido de " + tiempRes + " microsegundos o " + tiempNano + " nanosegundos");

        System.out.println("Termina de calcular");

        // Imprimir la matriz final
        //ecuacion.imprimirMatriz();
    }
}

class EcuacionDeOnda1DMonitor {
    private final double[][] matriz;
    private final boolean[][] calculado;
    private final Object[][] locks; // Usaremos esto para sincronizar cada celda
    private final int N;
    private final int T_max;
    private final double c;

    public EcuacionDeOnda1DMonitor(int N, int T_max, double v) {
        this.N = N;
        this.T_max = T_max;
        this.c = v * v;

        if(N < 0)
        {
            throw new IllegalArgumentException("N ha de ser un entero positivo");
        }

        if(T_max < 0)
        {
            throw new IllegalArgumentException("T_max ha de ser un entero positivo");
        }

        matriz = new double[N + 1][T_max + 1];
        calculado = new boolean[N + 1][T_max + 1];
        //lock = new ReentrantLock();
        //condiciones = new Condition[N + 1][T_max + 1];
        locks = new Object[N + 1][T_max + 1];

        // Inicializar las estructuras
        for (int i = 0; i <= N; i++) {
            for (int t = 0; t <= T_max; t++) {
                //condiciones[i][t] = lock.newCondition();
                locks[i][t] = new Object();
            }
        }

        // Inicializar la primera fila con valores predeterminados
        double j = 2.0;
        for (int i = 0; i <= N; i++) {
            matriz[i][0] = j;
            calculado[i][0] = true;
            j += 0.5;
            if (j == 10.0) {
                j = 2.0;
            }
        }
    }

    public double leerValor(int i, int t) throws InterruptedException {
        if (t < 0)
        {
            return 0.0;
        }
        if (i < 0)
        {
            i = 1; // Reflejo para Neumann en el borde inferior
        }
        if (i > N)
        {
            i = N - 1; // Reflejo para Neumann en el borde superior
        }
    
        synchronized (locks[i][t]) {
            while (!calculado[i][t]) {
                locks[i][t].wait();
            }
        }
        return matriz[i][t];
    }
    
    public void escribirValor(int i, int t, double valor) {
        synchronized (locks[i][t]) {
            matriz[i][t] = valor;
            calculado[i][t] = true;
            locks[i][t].notifyAll();
        }
    }

    public void calcularCelda(int i, int t) throws InterruptedException 
    {
        if (i < 0 || i > N) {
            throw new IllegalArgumentException("Primer parámetro fuera de rango");   //Por si i está fuera de rango
        }
        if (t <= 0 || t > T_max) {
            throw new IllegalArgumentException("Segundo parámetro fuera de rango");   //Por si t está fuera de rango
        }

        double valor = (2.0 * leerValor(i, t - 1)) - leerValor(i, t - 2) + (c * (leerValor(i - 1, t - 1) - (2.0 * leerValor(i, t - 1)) + leerValor(i + 1, t - 1)));
        escribirValor(i, t, valor);
    }

    public void imprimirMatriz() {
        System.out.printf("%10s", "i\\t");
        for (int t = 0; t <= T_max; t++) {
            System.out.printf("%10d", t);
        }
        System.out.println();

        for (int i = 0; i <= N; i++) {
            System.out.printf("%10d", i);
            for (int t = 0; t <= T_max; t++) {
                System.out.printf("%10.2f", matriz[i][t]);
            }
            System.out.println();
        }
    }
}

class OndaRunnable implements Runnable {
    private final EcuacionDeOnda1DMonitor ecuacion;
    private final int inicio;
    private final int fin;
    private final int T_max;

    public OndaRunnable(EcuacionDeOnda1DMonitor ecuacion, int inicio, int fin, int T_max) {
        this.ecuacion = ecuacion;
        this.inicio = inicio;
        this.fin = fin;
        this.T_max = T_max;
    }

    public void run() {
        try {
            for (int t = 1; t <= T_max; t++) {
                for (int i = inicio; i <= fin; i++) {
                    ecuacion.calcularCelda(i, t);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
