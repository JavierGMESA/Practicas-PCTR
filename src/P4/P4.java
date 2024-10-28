package P4;

import java.lang.Thread;

public class P4 {
    private volatile int n = 0; // IMPORTANTE: Las lecturas y escrituras de la variable se hacen en MP, no en
                                // caché (toda modificación se refleja al instante, pues cada procesador no mira
                                // en su propia caché si no que mira en MP). A la hora de leer nos aseguramos
                                // que el dato está actualizado, PERO PUEDE SEGUIR HABIENDO ENTRELAZADOS
    // Thread.yield() IMPORTANTE: Este método estático SUGIERE al sistema que puede
    // ponerse en pausa y dejar que el resto de hilos se ejecuten antes.
    // No es un mecanismo de sincronización pues el hilo puede seguir ejecutandose
    // dependiendo de la prioridad, del S.O. o de la JVM.

    public void ejercicio1() {

    }
}
