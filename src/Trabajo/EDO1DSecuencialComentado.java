package Trabajo;

/**
 * Clase que implementa el cálculo de una ecuación de onda 1D de manera secuencial.
 * 
 * <p>
 * La clase permite calcular y almacenar los valores de una ecuación de onda 
 * en una matriz, dados los parámetros N (máximo valor de i), T_max (máximo valor de t),
 * y v (velocidad de la onda).
 * </p>
 * 
 * @author Usuario
 * @version 1.0
 */
public class EcuacionDeOnda1DSecuencial {
    /** Matriz para almacenar los valores calculados de la ecuación */
    private double[][] matriz;
    /** Máximo valor de i (filas) */
    private int N;
    /** Máximo valor de t (columnas) */
    private int T_max;
    /** Parámetro c que depende del cuadrado de la velocidad de la onda */
    private double c;

    /**
     * Constructor para inicializar la matriz y los valores iniciales.
     * 
     * @param N el número máximo de filas (valor máximo de i)
     * @param T_max el número máximo de columnas (valor máximo de t)
     * @param v la velocidad de la onda
     * @throws IllegalArgumentException si N o T_max son negativos
     */
    public EcuacionDeOnda1DSecuencial(int N, int T_max, double v) {
        this.N = N;
        this.T_max = T_max;
        this.c = v * v;

        if (N < 0) {
            throw new IllegalArgumentException("N ha de ser un entero positivo");
        }

        if (T_max < 0) {
            throw new IllegalArgumentException("T_max ha de ser un entero positivo");
        }

        this.matriz = new double[N + 1][T_max + 1];

        // Inicialización de la primera columna (t=0) con valores aleatorios
        double j = 2.0;
        for (int i = 0; i <= N; i++) {
            matriz[i][0] = j;
            j += 0.5;
            if (j == 10.0) {
                j = 2.0;
            }
        }
    }

    /**
     * Calcula el valor de una celda en la matriz según la ecuación de ondas.
     * 
     * @param i índice de la fila
     * @param t índice de la columna
     * @throws IllegalArgumentException si i o t están fuera de rango
     */
    public void calcularCelda(int i, int t) {
        if (i < 0 || i > N) {
            throw new IllegalArgumentException("Primer parámetro fuera de rango");
        }
        if (t <= 0 || t > T_max) {
            throw new IllegalArgumentException("Segundo parámetro fuera de rango");
        }

        matriz[i][t] = (2.0 * leerValor(i, t - 1)) 
                     - leerValor(i, t - 2) 
                     + (c * (leerValor(i - 1, t - 1) - (2.0 * leerValor(i, t - 1)) + leerValor(i + 1, t - 1)));
    }

    /**
     * Lee un valor de la matriz manejando los bordes con condiciones de Neumann.
     * 
     * @param i índice de la fila
     * @param t índice de la columna
     * @return el valor leído, ajustado para bordes si es necesario
     */
    private double leerValor(int i, int t) {
        if (t < 0) {
            return 0.0; // Valores ficticios para t=-1
        }
        if (i < 0) {
            i = 1; // Reflejo para Neumann en el borde inferior
        }
        if (i > N) {
            i = N - 1; // Reflejo para Neumann en el borde superior
        }

        return matriz[i][t];
    }

    /**
     * Imprime la matriz calculada con encabezados para filas y columnas.
     */
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

    /**
     * Método principal para ejecutar la aplicación y probar los cálculos.
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        int N = 5000;
        int T_max = 10000;
        double v = 0.5;

        long tiempIni, tiempFin, tiempRes;

        EcuacionDeOnda1DSecuencial ecuacion = new EcuacionDeOnda1DSecuencial(N, T_max, v);

        tiempIni = System.nanoTime();
        for (int t = 1; t <= T_max; t++) {
            for (int i = 0; i <= N; i++) {
                ecuacion.calcularCelda(i, t);
            }
        }
        tiempFin = System.nanoTime();
        tiempRes = (tiempFin - tiempIni) / 1000;
        System.out.println("El tiempo del algoritmo secuencial con N=" + N + " y T_max=" + T_max + " ha sido de " + tiempRes + " microsegundos");

        System.out.println("Termina de calcular");

        // Ejecución de pruebas
        System.out.println("Pruebas:");
        try {
            ecuacion = new EcuacionDeOnda1DSecuencial(-1, T_max, v);
        } catch (Exception e) {
            System.out.println("Excepción 1: " + e.getMessage());
        }

        try {
            ecuacion = new EcuacionDeOnda1DSecuencial(N, -1, v);
        } catch (Exception e) {
            System.out.println("Excepción 2: " + e.getMessage());
        }

        try {
            ecuacion.calcularCelda(-3, 8);
        } catch (Exception e) {
            System.out.println("Excepción 3: " + e.getMessage());
        }

        try {
            ecuacion.calcularCelda(3, -3);
        } catch (Exception e) {
            System.out.println("Excepción 4: " + e.getMessage());
        }

        N = 0;
        T_max = 0;
        ecuacion = new EcuacionDeOnda1DSecuencial(N, T_max, v);

        for (int t = 1; t <= T_max; t++) {
            for (int i = 0; i <= N; i++) {
                ecuacion.calcularCelda(i, t);
            }
        }
    }
}

