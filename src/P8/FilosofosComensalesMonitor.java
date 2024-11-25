package P8;

public class FilosofosComensalesMonitor {
    private final int numFilosofos;
    private final Estado[] estados; // Estados de los filósofos: PENSANDO, HAMBRIENTO, COMIENDO

    // Definimos los estados
    private enum Estado {
        PENSANDO, HAMBRIENTO, COMIENDO
    }

    public FilosofosComensalesMonitor(int numFilosofos) {
        this.numFilosofos = numFilosofos;
        this.estados = new Estado[numFilosofos];

        // Inicializar todos los filósofos pensando
        for (int i = 0; i < numFilosofos; i++) {
            estados[i] = Estado.PENSANDO;
        }
    }

    // Método sincronizado para coger los palillos
    synchronized public void cogerPalillos(int filosofo) throws Exception {
        estados[filosofo] = Estado.HAMBRIENTO;
        verificar(filosofo); // Intenta comer
        System.out.println("Termina de verificar");
        while (estados[filosofo] != Estado.COMIENDO) 
        {
            wait(); // Espera a que pueda comer
        }
        System.out.println("Coge sus palillos");
    }

    // Método sincronizado para soltar los palillos
    synchronized public void soltarPalillos(int filosofo) {
        estados[filosofo] = Estado.PENSANDO; // Vuelve a pensar
        // Verifica si los vecinos pueden comer
        verificar(izquierda(filosofo));
        verificar(derecha(filosofo));
        notifyAll(); // Notifica a los filósofos que esperan
    }

    // Método para verificar si un filósofo puede comer
    private void verificar(int filosofo) {
        if (estados[filosofo] == Estado.HAMBRIENTO &&
            estados[izquierda(filosofo)] != Estado.COMIENDO &&
            estados[derecha(filosofo)] != Estado.COMIENDO) 
            {
                estados[filosofo] = Estado.COMIENDO;
                notifyAll(); // Notifica a los filósofos que pueden estar esperando
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

