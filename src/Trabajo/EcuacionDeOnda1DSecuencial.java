package Trabajo;

import java.util.concurrent.ThreadLocalRandom;

public class EcuacionDeOnda1DSecuencial {
    private double[][] matriz; // Matriz para almacenar los valores
    private int N; // Máximo valor de i (filas)
    private int T_max; // Máximo valor de t (columnas)
    private double c; // Parámetro c del calculo 

    // Constructor para inicializar la matriz y valores iniciales
    // Precondición: N y T_max han de ser positivos
    public EcuacionDeOnda1DSecuencial(int N, int T_max, double v) {
        this.N = N;
        this.T_max = T_max;
        this.c = v * v;

        //Caso en que se introduzcan un N o T_max negativo
        if(N < 0)
        {
            throw new IllegalArgumentException("N ha de ser un entero positivo");
        }

        if(T_max < 0)
        {
            throw new IllegalArgumentException("T_max ha de ser un entero positivo");
        }

        this.matriz = new double[N + 1][T_max + 1];

        // Inicialización de la primera columna (t=0) con valores aleatorios
        for (int i = 0; i <= N; i++) {
            matriz[i][0] = ThreadLocalRandom.current().nextDouble(-1000.0, 1000.0);
        }
    }

    // Método para rellenar la celda (i, t) según la ecuación de ondas
    // Precondición: 0 <= i <= N y 0 < t <= T_max
    public void calcularCelda(int i, int t) {
        if (i < 0 || i > N) {
            throw new IllegalArgumentException("Primer parámetro fuera de rango");   //Por si i está fuera de rango
        }
        if (t <= 0 || t > T_max) {
            throw new IllegalArgumentException("Segundo parámetro fuera de rango");   //Por si t está fuera de rango
        }

        // Ecuación de ondas
        matriz[i][t] = (2.0 * leerValor(i, t - 1)) - leerValor(i, t - 2)+ (c * (leerValor(i - 1, t - 1) - (2.0 * leerValor(i, t - 1)) + leerValor(i + 1, t - 1)));
    }

    // Método privado para leer valores de la matriz, manejando bordes con Neumann
    private double leerValor(int i, int t) {
        if (t < 0) {
            return 0; // Valores ficticios para t=-1
        }
        if (i < 0) {
            return leerValor(1, t); // Reflejo para Neumann en el borde superior
        }
        if (i > N) {
            return leerValor(N - 1, t); // Reflejo para Neumann en el borde inferior
        }
        return matriz[i][t];
    }

    // Método para imprimir la matriz con encabezados para i (filas) y t (columnas)
    public void imprimirMatriz() {
        // Imprimir encabezado de las columnas (valores de t)
        System.out.printf("%10s", "i\\t"); // Etiqueta de la esquina superior izquierda
        for (int t = 0; t <= T_max; t++) {
            System.out.printf("%10d", t); // Encabezado para cada columna
        }
        System.out.println();

        // Imprimir las filas con sus valores
        for (int i = 0; i <= N; i++) {
            System.out.printf("%10d", i); // Encabezado de la fila (valor de i)
            for (int t = 0; t <= T_max; t++) {
                System.out.printf("%10.2f", matriz[i][t]); // Valores de la matriz
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // Ejemplo de uso
        int N = 10; // Número de filas
        int T_max = 10; // Número de columnas
        double v = 0.5; // Velocidad de la onda

        EcuacionDeOnda1DSecuencial ecuacion = new EcuacionDeOnda1DSecuencial(N, T_max, v);

        // Ponemos un N negativo
        //ecuacion = new EcuacionDeOnda1DSecuencial(-1, T_max, v); // Lanza excepción "N ha de ser un entero positivo"

        //Ponemos un T_max negativo
        //ecuacion = new EcuacionDeOnda1DSecuencial(N, -1, v); // Lanza excepción "T_max ha de ser un entero positivo"

        // Rellenar la matriz
        for (int t = 1; t <= T_max; t++) {
            for (int i = 0; i <= N; i++) {
                ecuacion.calcularCelda(i, t);
            }
        }
        
        // Ponemos un i fuera de rango
        //ecuacion.calcularCelda(300, 8); // Lanza excepción "Primer parámetro fuera de rango"

        // Ponemos un t fuera de rango
        //ecuacion.calcularCelda(3, 300); // Lanza excepción "Segundo parámetro fuera de rango"

        // Imprimir la matriz resultante
        ecuacion.imprimirMatriz();
    }
}