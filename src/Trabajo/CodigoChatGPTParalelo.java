package Trabajo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class EcuacionDeOnda1DParalela {
    private final double[][] matriz;
    private final boolean[][] calculado;
    private final Object[][] locks; // Usaremos esto para sincronizar cada celda
    private final int N;
    private final int T_max;
    private final double c;

    public EcuacionDeOnda1DParalela(int N, int T_max, double v) {
        this.N = N;
        this.T_max = T_max;
        this.c = v * v;

        if (N < 0 || T_max < 0) {
            throw new IllegalArgumentException("N y T_max deben ser positivos");
        }

        matriz = new double[N + 1][T_max + 1];
        calculado = new boolean[N + 1][T_max + 1];
        locks = new Object[N + 1][T_max + 1];

        for (int i = 0; i <= N; i++) {
            for (int t = 0; t <= T_max; t++) {
                locks[i][t] = new Object();
            }
        }

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

    public boolean esValido(int i, int t) {         //NO HACE FALTA
        return i >= 0 && i <= N && t >= 0 && t <= T_max;
    }

    public double leerValor(int i, int t) throws InterruptedException {
        if (i < 0) i = 0;               // Usar el valor del primer índice
        if (i >= N) i = N - 1;          // Usar el valor del último índice

        if (t < 0) {
            return 0.0; // Valor predefinido para t < 0 (ajusta según tu modelo)
        }

        synchronized (locks[i][t]) {
            while (!calculado[i][t]) {
                locks[i][t].wait();
            }
            return matriz[i][t];
        }
    }

    public void escribirValor(int i, int t, double valor) {
        synchronized (locks[i][t]) {
            matriz[i][t] = valor;
            calculado[i][t] = true;
            locks[i][t].notifyAll();
        }
    }

    public void calcularCelda(int i, int t) throws InterruptedException {
        double valor = (2.0 * leerValor(i, t - 1)) - leerValor(i, t - 2) +
                (c * (leerValor(i - 1, t - 1) - (2.0 * leerValor(i, t - 1)) + leerValor(i + 1, t - 1)));
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
    private final EcuacionDeOnda1DParalela ecuacion;
    private final int inicio;
    private final int fin;
    private final int T_max;

    public OndaRunnable(EcuacionDeOnda1DParalela ecuacion, int inicio, int fin, int T_max) {
        this.ecuacion = ecuacion;
        this.inicio = inicio;
        this.fin = fin;
        this.T_max = T_max;
    }

    @Override
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

public class CodigoChatGPTParalelo {
    public static void main(String[] args) throws InterruptedException {
        int N = 10;
        int T_max = 10;
        double v = 0.5;
        int numHilos = 4;

        EcuacionDeOnda1DParalela ecuacion = new EcuacionDeOnda1DParalela(N, T_max, v);

        // Crear un ThreadPoolExecutor
        ExecutorService threadPool = Executors.newFixedThreadPool(numHilos);
        List<OndaRunnable> tareas = new ArrayList<>();

        // Dividir las tareas entre los hilos
        int rango = (N + 1) / numHilos;
        for (int h = 0; h < numHilos; h++) {
            final int inicio = h * rango;
            final int fin = (h == numHilos - 1) ? N : inicio + rango - 1;
            tareas.add(new OndaRunnable(ecuacion, inicio, fin, T_max));
        }

        // Enviar las tareas al ThreadPoolExecutor
        for (OndaRunnable tarea : tareas) {
            threadPool.execute(tarea);
        }

        // Cerrar el ThreadPoolExecutor
        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
            Thread.sleep(100); // Esperar a que todos los hilos terminen
        }

        // Imprimir la matriz final
        ecuacion.imprimirMatriz();
    }
}
