#ifndef PARINTEGRAL
#define PARINTEGRAL

#include <iostream>
#include <cassert>
#include <mutex>
#include <random>
#include <vector>
#include <thread>

class Monte_Carlo
{
public:
    Monte_Carlo(int numPoints): n_puntos{numPoints}, totalHits{0.0} {}
    static double f(double x);
    void calculoPuntos(int hits, int id_hebra);
    double solucion() {return totalHits / n_puntos;}
private:
    std::mutex cerrojo;
    double totalHits, n_puntos;
};

void monteCarloIntegration(int numPoints);


#endif