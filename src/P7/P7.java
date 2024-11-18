package P7;

public class P7 {
    public void ejercicio1()
    {
        //Primer apartado
        usaProdCon.ProductorConsumidor(1, 10, 10);
        //Ejecuta bien

        //Segundo apartado
        usaProdCon.ProductorConsumidor(10, 10, 1);
        //Se bloquea pues cada consumidor intenta consumir 100 elementos
        //mientras que el único productor produce 100 elementos
        
        //Tercer apartado
        usaProdCon.ProductorConsumidor(10, 1, 10);
        //Se bloquea pues al ser el buffer de tamaño finito, un consumidor
        //puede obtener 100 elementos solo mientras que cada productor produce 100, 
        //llenando el buffer así. Se puede solucionar haciendo el buffer ilimitado o
        //aumentando la cantidad de elementos a consumir.
    }

    public void ejercicio2()
    {
        usaLecEsc.LectorEscritor(10, 10);
    }
}
