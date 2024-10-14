package P2;
import java.lang.Thread;
import java.lang.InterruptedException;

public class P2 
{
    private int ej6n = 0;
    public void ejercicio1() throws InterruptedException
    {
        ej1 e1 = new ej1(true);
        ej1 e2 = new ej1(false);
        e1.start();
        e2.start();
        e1.join();
        e2.join();
        int n = e1.resultado();
        System.out.println("\nEl valor de n es " + n + "\n");
    }  

    public void ejercicio2() throws InterruptedException
    {
        ej2IncDec ej = new ej2IncDec();
        ej2 e1 = new ej2(ej, true);
        ej2 e2 = new ej2(ej, false);
        Thread h1 = new Thread(e1);
        Thread h2 = new Thread(e2);
        h1.start();
        h2.start();
        h1.join();
        h2.join();
        System.out.println("\nEl valor de n es " + ej.resultado() + "\n");
    }
    
    public void ejercicio3() throws InterruptedException
    {
        int es = 20;
        ej3Sec e1 = new ej3Sec();
        e1.escalar(es);
        System.out.println("Secuencial terminado");
        ej3Par e2 = new ej3Par(0, es);
        ej3Par e3 = new ej3Par(1, es);
        ej3Par e4 = new ej3Par(2, es);
        ej3Par e5 = new ej3Par(3, es);
        ej3Par.crearVect();
        Thread h1 = new Thread(e2);
        Thread h2 = new Thread(e3);
        Thread h3 = new Thread(e4);
        Thread h4 = new Thread(e5);
        h1.start();
        h2.start();
        h3.start();
        h4.start();
        h1.join();
        h2.join();
        h3.join();
        h4.join();
        System.out.println("Paralelo terminado\n");
    }

    public void ejercicio5() throws InterruptedException
    {
        CuentaCorriente c = new CuentaCorriente("1098", 100000);
        ej5inc e1 = new ej5inc(c);
        ej5dec e2 = new ej5dec(c);
        Thread h1 = new Thread(e1);
        Thread h2 = new Thread(e2);
        h1.start();
        h2.start();
        h1.join();
        h2.join();
        System.out.println("\nEl saldo de la cuenta es " + c.consultarSaldo() + "\n");
    }

    public void ejercicio6() throws InterruptedException
    {
        Runnable r1 = new Runnable() {
            @Override
            public void run()
            {
                for(int i = 0; i < 10000; ++i)
                ++ej6n;
            }
        };
        Runnable r2 = new Runnable() {
            @Override
            public void run()
            {
                for(int i = 0; i < 10000; ++i)
                --ej6n;
            }
        };
        Thread h1 = new Thread(r1);
        Thread h2 = new Thread(r2);
        h1.start();
        h2.start();
        h1.join();
        h2.join();
        System.out.println("\nEl valor de la variable es " + ej6n + "\n");
    }
}

class ej1 extends Thread // La clase 'ej1' extiende de Thread, permitiendo que cada instancia se ejecute en un hilo separado.
{
    private static int n = 0; // Atributo estático compartido entre todas las instancias. Aquí se puede crear una condición de carrera.
    private Boolean incre; // Variable que determina si la instancia incrementará o decrementará el valor de 'n'.

    // Constructor que recibe un valor booleano para definir el comportamiento (incrementar o decrementar).
    public ej1(Boolean inc)
    {
        incre = inc; // Asignamos el valor recibido a la variable 'incre'.
    }

    // Método 'run' que se ejecuta cuando se inicia el hilo (método principal del hilo).
    public void run()
    {
        if(incre) // Si 'incre' es verdadero, el hilo incrementará 'n'.
        {
            for(int i = 0; i < 10000; ++i) // Bucle que incrementa el valor de 'n' 10,000 veces.
            {
                ++n; // Incremento de 'n'. Esto es donde ocurre la condición de carrera, ya que varios hilos pueden modificar 'n' simultáneamente.
            }
        }
        else // Si 'incre' es falso, el hilo decrementará 'n'.
        {
            for(int i = 0; i < 10000; ++i) // Bucle que decrementa el valor de 'n' 10,000 veces.
            {
                --n; // Decremento de 'n'. Aquí también ocurre la condición de carrera.
            }
        }
    }

    // Método para obtener el valor actual de 'n' después de que los hilos hayan terminado.
    public int resultado()
    {
        return n; // Retorna el valor de 'n', que ha sido afectado por todos los hilos.
    }
}

//Si mostraramos la variable con cada incremento y decremento el acceso al objeto es mas lento
//por lo que es menos probable que se entrelacen (saldrá 0 seguramente). No hay condición de carrera.


class ej2 implements Runnable
{
    ej2IncDec ej;
    Boolean in;
    public ej2(ej2IncDec e, Boolean inc)
    {
        ej = e;
        in = inc;
    }

    public void run()
    {
        if(in)
        {
            for(int i = 0; i < 10000; ++i)
            {
                ej.inc();
            }
        }
        else
        {
            for(int i = 0; i < 10000; ++i)
            {
                ej.dec();
            }
        }
    }
    
}

class ej2IncDec
{
    private int n = 0;
    public void inc()
    {
        ++n;
    }
    public void dec()
    {
        --n;
    }
    public int resultado()
    {
        return n;
    }
}

class ej3Sec
{
    private int[] v;
    public ej3Sec()
    {
        v = new int[1000000];
        for(int i = 0; i < 1000000; ++i)
        {
            v[i] = 2000;
        }
    }
    public void escalar(int esc)
    {
        for(int i = 0; i < 1000000; ++i)
        {
            v[i] = v[i] * esc;
        }
    }
}

class ej3Par implements Runnable
{
    private static int[] v;
    private int id, esc;
    public ej3Par(int iden, int es)
    {
        id = iden;
        esc = es;
    }
    public static void crearVect()
    {
        v = new int[1000000];
        for(int i = 0; i < 1000000; ++i)
        {
            v[i] = 2000;
        }
    }
    public void run()
    {
        for(int i = id; i < 1000000; i = i + 4)
        {
            v[i] = v[i] * esc;
        }
    }
}

class CuentaCorriente {
    // Atributos
    private String numeroCuenta;
    private double saldo;

    // Constructor
    public CuentaCorriente(String numeroCuenta, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
    }

    // Método para realizar un depósito
    public void deposito(double monto) {
        if (monto > 0) {
            saldo += monto;
            System.out.println("Depósito realizado. Nuevo saldo: " + saldo);
        } else {
            System.out.println("El monto del depósito debe ser positivo.");
        }
    }

    // Método para realizar un reintegro (retiro)
    public void reintegro(double monto) {
        if (monto > 0 && monto <= saldo) {
            saldo -= monto;
            System.out.println("Reintegro realizado. Nuevo saldo: " + saldo);
        } else if (monto > saldo) {
            System.out.println("Fondos insuficientes.");
        } else {
            System.out.println("El monto del reintegro debe ser positivo.");
        }
    }

    // Método para consultar el saldo
    public double consultarSaldo() {
        return saldo;
    }

    // Método para obtener el número de cuenta
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    //EJEMPLO
    //public static void main(String[] args) {
        // Ejemplo de uso
        //CuentaCorrienteGPT cuenta = new CuentaCorrienteGPT("123456789", 500.0);
        //cuenta.deposito(200.0);
        //cuenta.reintegro(100.0);
        //System.out.println("Saldo actual: " + cuenta.consultarSaldo());
    //}
}

class ej5inc implements Runnable
{
    CuentaCorriente c;
    public ej5inc(CuentaCorriente c)
    {
        this.c = c;
    }
    
    public void run()
    {
        for(int i = 0; i < 10000; ++i)
        {
            c.deposito(500);
        }
    }
}

class ej5dec implements Runnable
{
    CuentaCorriente c;
    public ej5dec(CuentaCorriente c)
    {
        this.c = c;
    }
    
    public void run()
    {
        for(int i = 0; i < 10000; ++i)
        {
            c.reintegro(500);
        }
    }
}
