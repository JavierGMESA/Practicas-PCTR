package P4;

import java.lang.Thread;

public class Lamport extends Thread {
    private static volatile int[] C = new int[2];
    private static volatile long[] numero = new long[2];

    private int id;

    static int incremento = 0;
    private static int iteraciones = 10000;

    public Lamport(int id) {
        this.id = id;
        C[id] = 0;
        numero[id] = 0; // número a 0 indica que no tiene
    }

    public void run() {
        for (int i = 0; i < iteraciones; ++i) {
            C[id] = 1; // Quiere coger número

            numero[id] = 1 + Math.max(numero[0], numero[1]); // Se escoge el máximo de los números (habría que hacer
                                                             // un for si hubiera más de dos hilos)
            C[id] = 0; // Deja de coger número

            // Debe entrar primero el que tenga el número más bajo, y en caso de empate, el
            // que tenga el id más bajo
            for (int j = 0; j < 2; ++j) {
                while (C[j] == 1) { // Comprueba si el hilo que está comprobando está cogiendo número
                    Thread.yield();
                }
                while ((numero[j] != 0) && ((numero[j] < numero[id]) || ((numero[j] == numero[id]) && (j < id)))) {
                    Thread.yield();
                } // Debe esperar hasta que su número sea menor o que empate y su id sea menor
            }

            // Sección crítica
            ++incremento;

            numero[id] = 0;
        }
    }

    public static void main(String[] args) {
        try { // Debido al InterruptedException
            Lamport h1, h2;
            h1 = new Lamport(0);
            h2 = new Lamport(1);
            h1.start();
            h2.start();
            h1.join();
            h2.join();
            System.out.println("El valor del incremento es " + incremento);
        } catch (InterruptedException a) {
        }
    }
}

// El algoritmo de Lamport es sobre todo para problemas de sistemas distribuidos

// Se verifica la exclusión mutua porque todo hilo tendrá un número mayor que
// los que ya tienen número. Además, si 2 hilos tuvieran el mismo número iría
// antes el de menor id, por lo que no pueden entrar más de 1 a la vez en la
// sección crítica.

// El único fallo de este algoritmo sería si el número que escogen los procesos
// suspera alguna vez el límite del tipo.
