#include <iostream>
#include "parIntegral.hpp"
#include "simBarrier.hpp"

void Ej1();
void Ej2();

int main() 
{
    //Ej1();
    Ej2();
    std::cout << std::endl << std::endl << "System Pause" << std::endl;
}

void Ej1()
{
    int n_puntos;
    std::cout << "Ingrese el numero de puntos a lanzar: ";
    std::cin >> n_puntos;
    monteCarloIntegration(n_puntos);
}

void Ej2()
{
    simBarrier barrera(3);
    std::mutex cerrojo;
    std::thread h1(ejecucionHebras, std::ref(barrera), std::ref(cerrojo));
    std::thread h2(ejecucionHebras, std::ref(barrera), std::ref(cerrojo));
    std::thread h3(ejecucionHebras, std::ref(barrera), std::ref(cerrojo));
    h1.join();                                                                  //IMPORTANTE: EN C++ EL main DEBE SER EL ÚLTIMO HILO EN ACABAR SIEMPRE
    h2.join();
    h3.join();
    std::cout << "main reseteando barrera para tres nuevas hebras..." << std::endl;
    barrera.resetBarrier();
    std::thread h4(ejecucionHebras, std::ref(barrera), std::ref(cerrojo));
    std::thread h5(ejecucionHebras, std::ref(barrera), std::ref(cerrojo));
    std::thread h6(ejecucionHebras, std::ref(barrera), std::ref(cerrojo));
    h4.join();
    h5.join();
    h6.join();
}