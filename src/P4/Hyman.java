package P4;

public class Hyman {
    /* Number of processes currently in critical section */
    static volatile int inCS = 0;
    /* Process p wants to enter critical section */
    static volatile boolean wantp = false;
    /* Process q wants to enter critical section */
    static volatile boolean wantq = false;
    /* Which processes turn is it */
    static volatile int turn = 1;

    private int incremento = 0;
    private int iteraciones = 10000000;

    class P extends Thread {
        public void run() {
            for (int i = 0; i < iteraciones; ++i) {
                /* Non-critical section */
                wantp = true;                           //EL ALGORITMO PUEDE FALLAR PUES SI NO TIENE EL TURNO Y EL SEGUNDO HILO NO HA
                while (turn != 1) {                     //PUESTO wantq = true, PUEDE SALIR DEL SEGUNDO while. INMEDIATAMENTE DESPUÉS EL 
                    while (wantq) {                     //SEGUNDO HILO LLEGA A SU PRIMER WHILE Y COMO TIENE EL TURNO ENTRA EN LA SC.
                    }                                   //INMEDIATAMENTE DESPUÉS EL PRIMER HILO PONER turn = 1 Y ENTRA TAMBIÉN EN LA SC.
                    turn = 1;
                }
                inCS++;
                Thread.yield();
                /* Critical section */
                ++incremento;

                inCS--;
                wantp = false;
            }
        }
    }

    class Q extends Thread {
        public void run() {
            for (int i = 0; i < iteraciones; ++i) {
                /* Non-critical section */
                wantq = true;
                while (turn != 2) {
                    while (wantp) {
                    }
                    turn = 2;
                }
                inCS++;
                Thread.yield();
                /* Critical section */
                ++incremento;

                inCS--;
                wantq = false;
            }
        }
    }

    Hyman() throws InterruptedException {
        Thread p = new P();
        Thread q = new Q();
        p.start();
        q.start();
        p.join();
        q.join();
        System.out.println("El valor del incremento es " + incremento);
    }

    public static void main(String[] args) {
        try { // Debido al InterruptedException
            new Hyman();
        } catch (InterruptedException a) {
        }
    }
}
