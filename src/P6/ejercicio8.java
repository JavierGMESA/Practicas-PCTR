package P6;

public class ejercicio8 {
    public static void main(String[] args) throws Exception
    {
        Exclu_mutua1 e1 = new Exclu_mutua1();
        Exclu_mutua2 e2 = new Exclu_mutua2();
        Thread h1 = new Thread(e1);
        Thread h2 = new Thread(e1);
        Thread h3 = new Thread(e2);
        Thread h4 = new Thread(e2);
        h1.start();
        h2.start();
        h3.start();
        h4.start();
        h1.join();
        h2.join();
        h3.join();
        h4.join();
        e1.mostrar();
        e2.mostrar();
    }
}

class Exclu_mutua1 implements Runnable
{
    static int inc = 0;
    public void run()
    {
        for(int i = 0; i < 1000000; ++i)
        {
            incrementar(); //CÓDIGO AÑADIDO POR CHATGPT
        }
    }

    private synchronized void incrementar() //CÓDIGO AÑADIDO POR CHATGPT
    {
        ++inc;
    }

    public void mostrar()
    {
        System.out.println("El valor de la variable es " + inc);
    }
}

class Exclu_mutua2 implements Runnable
{
    static int inc = 0;
    public void run()
    {
        for(int i = 0; i < 1000000; ++i)
        {
            incrementar(); //CÓDIGO AÑADIDO POR CHATGPT
        }
    }

    private synchronized void incrementar() //CÓDIGO AÑADIDO POR CHATGPT
    {
        ++inc;
    }

    public void mostrar()
    {
        System.out.println("El valor de la variable es " + inc);
    }
}
