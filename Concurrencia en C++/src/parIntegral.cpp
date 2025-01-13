#include "parIntegral.hpp"

double Monte_Carlo::f(double x)
{
    return (x * x * x) / (x * x * x * x + 2);
}

void Monte_Carlo::calculoPuntos(int puntos, int id_hebra)
{
    double punto;
    double hits = 0;
    std::random_device rd;
    std::mt19937 generador(rd() + id_hebra);
    std::uniform_int_distribution<int> dis(0, 1000);
    for(int i = 0; i < puntos; ++i)
    {
        punto = dis(generador) / 1000.00;
        punto = f(punto);
        hits += punto;
    }
    {
        std::unique_lock<std::mutex> em(cerrojo);
        totalHits += hits;
    }
}

void monteCarloIntegration(int numPoints)
{
    int n_hebras = 0;
    std::cout << std::endl << "Ingrese el numero de tareas paralelas: ";    //IMPORTANTE: EN UN PROGRAMA CONCURRENTE EN C++, EL cout Y cin HAY QUE
    std::cin >> n_hebras;                                                   //PROTEGERLO BAJO EXCLUSIÓN MUTUA
    assert(numPoints >= n_hebras);
    std::vector<std::thread> ejecutor;
    Monte_Carlo m(numPoints);
    for(int i = 0; i < n_hebras; ++i)
    {
        ejecutor.push_back(std::thread(&Monte_Carlo::calculoPuntos, &m, numPoints/n_hebras, i));
    }
    for(int i = 0; i < n_hebras; ++i)
    {
        ejecutor[i].join();
    }
    std::cout << "El valor de la solución es " << m.solucion() << std::endl;
}
