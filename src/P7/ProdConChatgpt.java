package P7;

import java.util.LinkedList;
import java.util.Queue;

public class ProdConChatgpt {

    private final Queue<Integer> buffer;
    private final int capacidad;

    public ProdConChatgpt(int capacidad) {
        this.capacidad = capacidad;
        this.buffer = new LinkedList<>();
    }

    // Método para producir
    public synchronized void producir(int item) throws InterruptedException {
        while (buffer.size() == capacidad) {
            System.out.println("Buffer lleno. El productor espera...");
            wait(); // Espera a que haya espacio en el buffer
        }
        buffer.add(item);
        System.out.println("Producido: " + item);
        notifyAll(); // Notifica a los consumidores que hay un nuevo elemento
    }

    // Método para consumir
    public synchronized int consumir() throws InterruptedException {
        while (buffer.isEmpty()) {
            System.out.println("Buffer vacío. El consumidor espera...");
            wait(); // Espera a que haya elementos en el buffer
        }
        int item = buffer.poll();
        System.out.println("Consumido: " + item);
        notifyAll(); // Notifica a los productores que hay espacio disponible
        return item;
    }

    // Método principal para probar el monitor
    public static void main(String[] args) {
        ProdConChatgpt monitor = new ProdConChatgpt(5); // Buffer de tamaño 5

        // Hilo productor
        Thread productor = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    monitor.producir(i);
                    Thread.sleep(500); // Simula tiempo de producción
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Hilo consumidor
        Thread consumidor = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    monitor.consumir();
                    Thread.sleep(800); // Simula tiempo de consumo
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        productor.start();
        consumidor.start();
    }
}

