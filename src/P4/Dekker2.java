package P4;

import java.lang.Thread;

public class Dekker2 {
    /* Number of processes currently in critical section */
    static volatile int inCS = 0;
    /* Process p wants to enter critical section */
    static volatile boolean wantp = false;
    /* Process q wants to enter critical section */
    static volatile boolean wantq = false;
    /* Which processes turn is it */
    static volatile int turn = 1;

    private int incremento = 0;
    private int iteraciones = 10000;

    class P extends Thread {
        public void run() {
            for (int i = 0; i < iteraciones; ++i) {
                /* Non-critical section */
                wantp = true;
                while (wantq) {
                    if (turn == 2) {
                        wantp = false;
                        while (turn == 2)
                            Thread.yield();
                        wantp = true;
                    }
                }
                inCS++;
                Thread.yield();
                /* Critical section */
                ++incremento;

                inCS--;
                turn = 2;
                wantp = false;
            }
        }
    }

    class Q extends Thread {
        public void run() {
            for (int i = 0; i < iteraciones; ++i) {
                /* Non-critical section */
                wantq = true;
                while (wantp) {
                    if (turn == 1) {
                        wantq = false;
                        while (turn == 1)
                            Thread.yield();
                        wantq = true;
                    }
                }
                inCS++;
                Thread.yield();
                /* Critical section */
                ++incremento;

                inCS--;
                turn = 1;
                wantq = false;
            }
        }
    }

    Dekker2() throws InterruptedException {
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
            new Dekker2();
        } catch (InterruptedException a) {
        }
    }
}
