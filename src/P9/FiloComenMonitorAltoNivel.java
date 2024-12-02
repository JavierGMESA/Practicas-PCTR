package P9;

import java.util.concurrent.locks.*;

public class FiloComenMonitorAltoNivel 
{
    private final int numFilosofos;
    private final Estado[] estados; // Estados de los filósofos: PENSANDO, HAMBRIENTO, COMIENDO
    ReentrantLock l;
    Condition[] dormir;

    // Definimos los estados
    private enum Estado {
        PENSANDO, HAMBRIENTO, COMIENDO
    }

    public FiloComenMonitorAltoNivel(int numFilosofos) {
        this.numFilosofos = numFilosofos;
        this.estados = new Estado[numFilosofos];
        l = new ReentrantLock();
        dormir = new Condition[numFilosofos];

        // Inicializar todos los filósofos pensando
        for (int i = 0; i < numFilosofos; i++) {
            estados[i] = Estado.PENSANDO;
            dormir[i] = l.newCondition();
        }
    }

    // Método sincronizado para coger los palillos
    public void cogerPalillos(int filosofo) throws Exception {
        l.lock();
        try
        {
            estados[filosofo] = Estado.HAMBRIENTO;
            verificar(filosofo); // Intenta comer
            System.out.println("Termina de verificar");
            while (estados[filosofo] != Estado.COMIENDO) 
            {
                dormir[filosofo].await(); // Espera a que pueda comer
            }
            System.out.println("Coge sus palillos");
        }finally
        {
            l.unlock();
        }
    }

    // Método sincronizado para soltar los palillos
    public void soltarPalillos(int filosofo) {
        l.lock();
        try
        {
            estados[filosofo] = Estado.PENSANDO; // Vuelve a pensar
            // Verifica si los vecinos pueden comer
            verificar(izquierda(filosofo));
            verificar(derecha(filosofo));
        }finally
        {
            l.unlock();
        }
    }

    // Método para verificar si un filósofo puede comer
    private void verificar(int filosofo) {
        if (estados[filosofo] == Estado.HAMBRIENTO &&
            estados[izquierda(filosofo)] != Estado.COMIENDO &&
            estados[derecha(filosofo)] != Estado.COMIENDO) 
            {
                estados[filosofo] = Estado.COMIENDO;
                dormir[filosofo].signal(); // Notifica al filósofo
            }
    }

    // Calcula el índice del filósofo de la izquierda
    private int izquierda(int filosofo) {
        return (filosofo + numFilosofos - 1) % numFilosofos;
    }

    // Calcula el índice del filósofo de la derecha
    private int derecha(int filosofo) {
        return (filosofo + 1) % numFilosofos;
    }
}
