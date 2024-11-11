package P6;

public class deadlock extends Thread{
    private static Object ob1 = new Object(), ob2 = new Object(), ob3 = new Object();
    private int metodo;

    public deadlock(int met)
    {
        metodo = met % 3;
    }

    public void run()
    {
        switch(metodo)
        {
            case 0: metodo1(); break;
            case 1: metodo2(); break;
            case 2: metodo3(); break;
        }
    }

    public void metodo1()
    {
        synchronized(ob1)
        {
            synchronized(ob2)
            {
                synchronized(ob3)
                {
                    System.out.println("Fin método 1");
                }
            }
        }
    }

    public void metodo2()
    {
        synchronized(ob2)
        {
            synchronized(ob3)
            {
                synchronized(ob1)
                {
                    System.out.println("Fin método 2");
                }
            }
        }
    }

    public void metodo3()
    {
        synchronized(ob3)
        {
            synchronized(ob1)
            {
                synchronized(ob2)
                {
                    System.out.println("Fin método 3");
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        deadlock d1, d2, d3;
        d1 = new deadlock(0);
        d2 = new deadlock(2);
        d3 = new deadlock(1);
        d1.start();
        d2.start();
        d3.start();
        d1.join();
        d2.join();
        d3.join();
        System.out.println("Todo se ha ejecutado sin interbloqueos");
    }
}
