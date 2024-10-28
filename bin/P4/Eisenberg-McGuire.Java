package P4;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

class EisenbergMcGuire {
    private static final int REMAINDER_SECTION = 0;  // Estado: fuera de la sección crítica
    private static final int WANT_IN = 1;            // Estado: queriendo entrar
    private static final int IN_CS = 2;              // Estado: dentro de la sección crítica

    private final int numProcesos;
    private final AtomicInteger turn = new AtomicInteger(-1);
    private final int[] estado;

    public EisenbergMcGuire(int numProcesos) {
        this.numProcesos = numProcesos;
        this.estado = new int[numProcesos];
        for (int i = 0; i < numProcesos; i++) {
            estado[i] = REMAINDER_SECTION;
        }
    }

    public void entrarSeccionCritica(int i) {
        estado[i] = WANT_IN;

        while (true) {
            int k = turn.get();
            while (k != -1) {
                if (k == i) {
                    break;  // Si es el turno del proceso i, intenta entrar
                }
                k = turn.get();
            }

            estado[i] = IN_CS; // Intenta entrar a la sección crítica
            boolean puedeEntrar = true;

            // Verifica si hay procesos de menor índice queriendo entrar
            for (int j = 0; j < numProcesos; j++) {
                if (j != i && estado[j] == IN_CS) {
                    puedeEntrar = false;
                    break;
                }
                if (j != i && estado[j] == WANT_IN && j < i) {
                    puedeEntrar = false;
                    turn.set(j);  // Cede el turno a un proceso de menor índice
                    break;
                }
            }

            if (puedeEntrar) {
                return;  // Entra en la sección crítica
            }

            // Espera activa si no puede entrar
            while (estado[turn.get()] != REMAINDER_SECTION) {
                // Espera activa
            }
        }
    }

    public void salirSeccionCritica(int i) {
        estado[i] = REMAINDER_SECTION;
        turn.set(-1); // Libera el turno para que otros procesos puedan entrar
    }
}

// Clase de prueba para ejecutar el algoritmo
public class PruebaEisenbergMcGuire {
    public static void main(String[] args) {
        int numProcesos = 3; // Número de procesos
        EisenbergMcGuire em = new EisenbergMcGuire(numProcesos);

        Runnable tarea = () -> {
            int procesoId = Integer.parseInt(Thread.currentThread().getName());
            for (int j = 0; j < 5; j++) {
                em.entrarSeccionCritica(procesoId);
                System.out.println("Proceso " + procesoId + " está en la sección crítica.");
                // Simula trabajo en la sección crítica
                try {
                    Thread.sleep((long) (Math.random() * 100));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                em.salirSeccionCritica(procesoId);
                System.out.println("Proceso " + procesoId + " ha salido de la sección crítica.");
            }
        };

        // Creación de hilos para simular procesos
        Thread[] hilos = new Thread[numProcesos];
        IntStream.range(0, numProcesos).forEach(i -> {
            hilos[i] = new Thread(tarea, String.valueOf(i));
            hilos[i].start();
        });

        // Espera a que todos los hilos terminen
        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}


//El algoritmo funciona así:
//1. El hilo indica que quiere entrar en la sección crítica.
//2. El hilo comprueba si tiene el turno o si no hay preferencia. 
//   Si se cumple una de estas 2 indica que va a entrar (IN_CS).
//3. Verifica si hay otros hilos en la sección crítica o si hay hilos
//   de menor índice queriendo entrar. Si esto ocurre, el hilo deja de
//   decir que puede entrar en la sección crítica, le da el turno al hilo y vuelve
//   a esperar a que le den el turno. Si puede entrar de forma definitiva entra.
//4. Cuando sale de la sección crítica indica que ya no quiere entrar en la sección
//   crítica y pone el turno a "sin preferencia".

//No hay riesgo a interbloqueo ni a que no haya exclusión mutua porque si 2 o más hilos
//pasan del primer bucle al comienzo porque no hay preferencia, cuando lleguen al primer for,
//el de mayor índice encontrará al de menor índice queriendo entrar y se pondrá de nuevo a esperar,
//dejando al de menor índice pasar a la sección crítica. 